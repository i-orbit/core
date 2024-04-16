package com.inmaytide.orbit.core.executor;

import com.inmaytide.orbit.commons.utils.CodecUtils;
import com.inmaytide.orbit.core.domain.FileMetadata;
import com.inmaytide.orbit.core.service.dto.Base64File;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

/**
 * @author inmaytide
 * @since 2024/4/8
 */
public class Base64FileUploader implements FileUploader {

    private final Base64File content;

    private final String bucket;

    public Base64FileUploader(Base64File content) {
        this(FILE_LIBRARY_BUCKET, content);
    }

    public Base64FileUploader(String bucket, Base64File content) {
        this.bucket = bucket;
        this.content = content;
    }

    @Override
    public FileMetadata call() throws Exception {
        String extension = FilenameUtils.getExtension(content.getName());
        Path file = Files.createTempFile(CodecUtils.randomUUID(), extension);
        Files.write(file, Base64.getDecoder().decode(content.getFile()), StandardOpenOption.CREATE_NEW);
        return upload(content.getName(), file);
    }

    @Override
    public String getBucket() {
        return bucket;
    }
}
