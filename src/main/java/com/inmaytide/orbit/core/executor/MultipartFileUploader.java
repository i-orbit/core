package com.inmaytide.orbit.core.executor;

import com.inmaytide.orbit.commons.utils.CodecUtils;
import com.inmaytide.orbit.core.domain.FileMetadata;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author inmaytide
 * @since 2024/4/8
 */
public class MultipartFileUploader implements FileUploader {

    private final MultipartFile content;

    private final String bucket;

    public MultipartFileUploader(MultipartFile content) {
        this(FILE_LIBRARY_BUCKET, content);
    }

    public MultipartFileUploader(String bucket, MultipartFile content) {
        this.bucket = bucket;
        this.content = content;
    }

    @Override
    public FileMetadata call() throws Exception {
        String extension = FilenameUtils.getExtension(content.getOriginalFilename());
        Path file = Files.createTempFile(CodecUtils.randomUUID(), extension);
        try (InputStream is = content.getInputStream()) {
            Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
        }
        return upload(content.getOriginalFilename(), file);
    }

    @Override
    public String getBucket() {
        return bucket;
    }
}
