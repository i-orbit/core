package com.inmaytide.orbit.core.utils;

import com.inmaytide.orbit.commons.utils.ApplicationContextHolder;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inmaytide
 * @since 2024/4/11
 */
public class MinioUtils {

    private static final String PATH_SEPARATOR = "/";

    private static CustomizedMinioClient minioClient;

    private MinioUtils() {

    }

    public static String getAddress(String bucket, String folder, String filename) {
        return bucket + PATH_SEPARATOR + getObjectName(folder, filename);
    }

    public static String getObjectName(String folder, String filename) {
        folder = StringUtils.removeStart(folder, PATH_SEPARATOR);
        folder = StringUtils.removeEnd(folder, PATH_SEPARATOR);
        filename = StringUtils.removeStart(filename, PATH_SEPARATOR);
        return folder + PATH_SEPARATOR + filename;
    }

    public static String getBucket(String address) {
        return address.substring(0, address.indexOf("/"));
    }

    public static String getObjectName(String address) {
        return address.substring(address.indexOf("/") + 1);
    }

    public static CustomizedMinioClient getMinioClient() {
        if (minioClient == null) {
            minioClient = ApplicationContextHolder.getInstance().getBean(CustomizedMinioClient.class);
        }
        return minioClient;
    }
}
