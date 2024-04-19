package com.inmaytide.orbit.core.configuration;

import com.inmaytide.orbit.commons.configuration.GlobalProperties;
import com.inmaytide.orbit.commons.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author inmaytide
 * @since 2023/4/12
 */
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties extends GlobalProperties {

    private MinIO minIO;

    private FileUploaderProperties fileUploader;

    public MinIO getMinIO() {
        return minIO;
    }

    public void setMinIO(MinIO minIO) {
        this.minIO = minIO;
    }

    public FileUploaderProperties getFileUploader() {
        return fileUploader;
    }

    public void setFileUploader(FileUploaderProperties fileUploader) {
        this.fileUploader = fileUploader;
    }

    public static class MinIO {

        private String endpoint;

        private String accessKey;

        private String secretKey;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

    }



}
