package com.inmaytide.orbit.core.executor;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inmaytide.orbit.commons.constants.Bool;
import com.inmaytide.orbit.commons.metrics.JobAdapter;
import com.inmaytide.orbit.commons.utils.CodecUtils;
import com.inmaytide.orbit.commons.utils.NamedStopWatch;
import com.inmaytide.orbit.core.configuration.FileUploaderProperties;
import com.inmaytide.orbit.core.consts.FileCategory;
import com.inmaytide.orbit.core.domain.FileMetadata;
import com.inmaytide.orbit.core.mapper.FileMetadataMapper;
import com.inmaytide.orbit.core.utils.CustomizedMinioClient;
import com.inmaytide.orbit.core.utils.FileUploadUtils;
import com.inmaytide.orbit.core.utils.MinioUtils;
import io.minio.DownloadObjectArgs;
import io.minio.RemoveObjectArgs;
import org.apache.commons.io.FilenameUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Scheduled task for verifying the SHA-256 checksums of large files and generating thumbnails.
 * <p>
 * Since the SHA-256 value of large files is provided by the frontend during upload,
 * the backend cannot verify the integrity or generate thumbnails in real time.
 * This scheduled task handles those files that are pending verification.
 * </p>
 *
 * <p><b>Execution steps:</b></p>
 * <ol>
 *     <li>Query all non-deleted files that are not yet verified and process them one by one</li>
 *     <li>Download each file to a temporary local directory</li>
 *     <li>Compute the actual SHA-256 value and compare it to the stored value</li>
 *     <li>
 *         If the values match:
 *         <ul>
 *             <li>Update the verification status to "verified"</li>
 *             <li>If supported, generate and save the corresponding thumbnail</li>
 *         </ul>
 *     </li>
 *     <li>
 *         If the values do not match:
 *         <ul>
 *             <li>Update the verification status to "failed"</li>
 *             <li>Delete the file from MinIO storage</li>
 *             <li>Remove all associated references to the file</li>
 *             <li>Send an email notification to system administrators</li>
 *         </ul>
 *     </li>
 *     <li>Delete the temporary local file to free up resources</li>
 * </ol>
 *
 * @author inmaytide
 * @since 2024/4/10
 */
public class FileValidator implements JobAdapter {

    private final Logger log = LoggerFactory.getLogger(FileValidator.class);

    @Autowired
    private FileMetadataMapper fileMetadataMapper;

    @Autowired
    private CustomizedMinioClient minioClient;

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getName() {
        return "timer-file_validator";
    }

    @Override
    public void exec(JobExecutionContext context, NamedStopWatch stopWatch) {
        List<FileMetadata> list = fileMetadataMapper.selectList(Wrappers.lambdaQuery(FileMetadata.class).eq(FileMetadata::getVerified, Bool.N));
        if (list.isEmpty()) {
            log.debug("No unverified files");
            return;
        }
        list.forEach(this::validate);
    }

    private void validate(FileMetadata metadata) {
        log.debug("Start verifying file{id = {}}", metadata.getId());
        String bucket = metadata.getAddress().substring(0, metadata.getAddress().indexOf("/"));
        String objectName = metadata.getAddress().substring(metadata.getAddress().indexOf("/") + 1);
        Path file;
        try {
            file = Files.createTempFile(CodecUtils.randomUUID(), "." + metadata.getExtension());
            DownloadObjectArgs args = DownloadObjectArgs.builder().filename(file.toAbsolutePath().toString()).bucket(bucket).object(objectName).build();
            minioClient.downloadObject(args).get();
        } catch (Exception e) {
            log.error("文件{id = {}}处理失败，下载文件时发生错误, Cause by: ", metadata.getId(), e);
            return;
        }
        String actualSHA256 = CodecUtils.getSHA256Value(file);
        if (Objects.equals(actualSHA256, metadata.getSha256())) {
            onMatched(metadata, file, bucket, objectName);
        } else {
            onMismatched(metadata, bucket, objectName);
        }
        FileUploadUtils.deleteQuietly(file);
    }

    private void onMismatched(FileMetadata metadata, String bucket, String objectName) {
        log.debug("文件{id = {}}无效", metadata.getId());
        metadata.setVerified(Bool.Y);
        metadata.setDeleted(Bool.Y);
        metadata.setDeleteTime(Instant.now());
        fileMetadataMapper.updateById(metadata);
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder().bucket(bucket).object(objectName).build();
            minioClient.removeObject(args);
        } catch (Exception e) {
            log.error("文件{id = {}}从MinIO中删除失败, Cause by: ", metadata.getId(), e);
        }
    }

    private void onMatched(FileMetadata metadata, Path file, String bucket, String objectName) {
        log.debug("文件{id = {}}验证成功", metadata.getId());
        metadata.setVerified(Bool.Y);
        Optional<ThumbnailGenerator> thumbnailGenerator = FileUploadUtils.getThumbnailGenerator(file);
        if (thumbnailGenerator.isPresent()) {
            try {
                String thumbnailAddress = FilenameUtils.removeExtension(objectName) + thumbnailGenerator.get().getOutputNameSuffix();
                Path thumbnail = thumbnailGenerator.get().generate(file);
                FileUploadUtils.upload(bucket, thumbnailAddress, thumbnail);
                metadata.setThumbnailAddress(MinioUtils.getAddress(bucket, thumbnailAddress));
            } catch (Exception e) {
                log.error("文件{id = {}}生成缩略图失败, Cause by: ", metadata.getId(), e);
            }
        }
        fileMetadataMapper.updateById(metadata);
    }
}
