package com.inmaytide.orbit.core.utils;

import com.inmaytide.orbit.commons.utils.ApplicationContextHolder;
import com.inmaytide.orbit.core.configuration.ApplicationProperties;
import com.inmaytide.orbit.core.configuration.FileUploaderProperties;
import com.inmaytide.orbit.core.consts.FileCategory;
import com.inmaytide.orbit.core.executor.ThumbnailGenerator;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 文件上传的一些工具方法
 *
 * @author inmaytide
 * @since 2022/8/25
 */
public final class FileUploadUtils {

    private static FileUploaderProperties.Extensions allowedExtensions;

    private FileUploadUtils() {

    }

    public static boolean isAllowExtension(String extension) {
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        final String forMatching = StringUtils.startsWith(extension, ".") ? extension.substring(1) : extension;
        return Stream.of(getAllowedExtensions().all())
                .flatMap(Collection::stream)
                .anyMatch(ext -> ext.equalsIgnoreCase(forMatching));
    }

    public static boolean isFileCategory(FileCategory category, String extension) {
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        return getAllowedExtensions().get(category).contains(extension);
    }

    /**
     * 检查MinIO中是否已存在写入目标桶，如果不存在则新建该桶
     */
    public static void createBucketIfNotExist(String bucket) throws Exception {
        if (MinioUtils.getMinioClient().bucketExists(BucketExistsArgs.builder().bucket(bucket).build()).get()) {
            return;
        }
        MinioUtils.getMinioClient().makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }

    public static void upload(String bucket, String address, Path file) throws Exception {
        createBucketIfNotExist(bucket);
        PutObjectArgs args = PutObjectArgs.builder().bucket(bucket)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .object(address)
                .stream(Files.newInputStream(file), Files.size(file), -1)
                .build();
        MinioUtils.getMinioClient().putObject(args);
    }

    public static long getSingleFileMaximumSize() {
        return ApplicationContextHolder.getInstance()
                .getBean(ApplicationProperties.class)
                .getFileUploader()
                .getSingleFileMaximumSize();
    }

    public static FileUploaderProperties.Extensions getAllowedExtensions() {
        if (allowedExtensions == null) {
            allowedExtensions = ApplicationContextHolder.getInstance()
                    .getBean(ApplicationProperties.class)
                    .getFileUploader()
                    .getAllowedExtensions();
        }
        return allowedExtensions;
    }

    public static Optional<ThumbnailGenerator> getThumbnailGenerator(Path file) {
        return ThumbnailGenerator.ALL_GENERATOR_INSTANCES.stream().filter(e -> e.support(file)).findFirst();
    }

    public static void delete(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
