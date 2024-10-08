package com.inmaytide.orbit.core.executor;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inmaytide.orbit.commons.constants.Bool;
import com.inmaytide.orbit.commons.metrics.JobAdapter;
import com.inmaytide.orbit.commons.utils.CodecUtils;
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
 * 大文件上传时文件的SHA256值是由前端传入，后端无法及时验证机生成缩略图；
 * 所以需要定时任务定时验证这些文件的SHA256值和生成相关缩略图信息
 * <br/><br/>
 * 执行步骤
 * <ol>
 *     <li>查询系统中所有未删除出且未验证的文件信息列表, 遍历执行后续操作</li>
 *     <li>下载文件到本地临时目录</li>
 *     <li>验证文件的SHA256值是否与已存入值相等</li>
 *     <li>相等 - 更新验证状态为已验证, 判断是否需要生成缩略图, 如果需要生成缩略图并保存</li>
 *     <li>无效 - 更新验证状态，因为验证无效时文件可能是通过非法途径上传，直接删除MinIO中存储的文件，并将所有文件引用删除，发送邮件通知系统管理员</li>
 *     <li>删除本地临时文件，释放资源</li>
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
    public void exec(JobExecutionContext context) {
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
            onSuccess(metadata, file, bucket, objectName);
        } else {
            onFailed(metadata, bucket, objectName);
        }
        FileUploadUtils.deleteQuietly(file);
    }

    private void onFailed(FileMetadata metadata, String bucket, String objectName) {
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

    private void onSuccess(FileMetadata metadata, Path file, String bucket, String objectName) {
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
