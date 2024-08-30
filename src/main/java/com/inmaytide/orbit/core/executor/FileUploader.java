package com.inmaytide.orbit.core.executor;

import com.inmaytide.exception.web.BadRequestException;
import com.inmaytide.orbit.commons.constants.Bool;
import com.inmaytide.orbit.commons.utils.ApplicationContextHolder;
import com.inmaytide.orbit.commons.utils.CodecUtils;
import com.inmaytide.orbit.commons.utils.DatetimeUtils;
import com.inmaytide.orbit.core.configuration.ErrorCode;
import com.inmaytide.orbit.core.domain.FileMetadata;
import com.inmaytide.orbit.core.service.FileMetadataService;
import com.inmaytide.orbit.core.utils.FileUploadUtils;
import com.inmaytide.orbit.core.utils.MinioUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
public interface FileUploader extends Callable<FileMetadata> {

    String FILE_LIBRARY_BUCKET = "file-library";

    default long getAndValidateFileSize(Path file) throws IOException {
        long size = Files.size(file);
        if (size <= 0) {
            throw new BadRequestException(ErrorCode.E_0x00300002);
        }
        if (size > FileUploadUtils.getSingleFileMaximumSize()) {
            throw new BadRequestException(ErrorCode.E_0x00300001);
        }
        return size;
    }

    default String getAndValidateExtension(String filename) {
        String extension = FilenameUtils.getExtension(filename);
        if (!FileUploadUtils.isAllowedExtension(extension)) {
            throw new BadRequestException(ErrorCode.E_0x00300003);
        }
        return extension;
    }

    default FileMetadata upload(String filename, Path file) throws Exception {
        // 验证文件SHA256值, 如果文件已存在直接返回文件元数据
        String sha256 = CodecUtils.getSHA256Value(file);
        Optional<FileMetadata> exist = getFileMetadataService().findBySHA256(sha256);
        if (exist.isPresent()) {
            return exist.get();
        }
        // 验证上传的文件是否是系统支持的文件类型
        String extension = getAndValidateExtension(filename);
        // 验证上传的文件大小是否超过限制
        long size = getAndValidateFileSize(file);
        String folder = DatetimeUtils.formatDateWithoutJoiner(Instant.now());
        String randomName = CodecUtils.generateRandomString(32);
        String thumbnailRandomName = null;
        FileUploadUtils.upload(getBucket(), MinioUtils.getObjectName(folder, randomName + "." + extension), file);
        Optional<ThumbnailGenerator> thumbnailGenerator = FileUploadUtils.getThumbnailGenerator(file);
        if (thumbnailGenerator.isPresent()) {
            thumbnailRandomName = randomName + thumbnailGenerator.get().getOutputNameSuffix();
            Path thumbnail = thumbnailGenerator.get().generate(file);
            FileUploadUtils.upload(getBucket(), MinioUtils.getObjectName(folder, thumbnailRandomName), thumbnail);
        }
        FileUploadUtils.deleteQuietly(file);
        return FileMetadata.builder()
                .filename(filename)
                .size(size)
                .sha256(sha256)
                .address(MinioUtils.getAddress(getBucket(), folder, randomName + "." + extension))
                .thumbnailAddress(thumbnailRandomName == null ? null : MinioUtils.getAddress(getBucket(), folder, thumbnailRandomName))
                .verified(Bool.Y)
                .build();
    }

    default FileMetadataService getFileMetadataService() {
        return ApplicationContextHolder.getInstance().getBean(FileMetadataService.class);
    }

    String getBucket();

}
