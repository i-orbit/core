package com.inmaytide.orbit.core.utils;

import com.inmaytide.orbit.commons.utils.ApplicationContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author inmaytide
 * @since 2024/4/11
 */
public final class MinioUtils {

    private static CustomizedMinioClient minioClient;

    private MinioUtils() {

    }

    public static String getAddress(String bucket, String folder, String filename) {
        return getAddress(bucket, getObjectName(folder, filename));
    }

    public static String getAddress(String bucket, String objectName) {
        return bucket + File.separator + objectName;
    }

    public static String getObjectName(String folder, String filename) {
        folder = StringUtils.removeStart(folder, File.separator);
        folder = StringUtils.removeEnd(folder, File.separator);
        filename = StringUtils.removeStart(filename, File.separator);
        return folder + File.separator + filename;
    }

    public static String getBucket(String address) {
        return StringUtils.removeStart(address, File.separator);
    }

    public static String getObjectName(String address) {
        return StringUtils.substring(address, address.indexOf(File.separator) + 1);
    }

    public static CustomizedMinioClient getMinioClient() {
        if (minioClient == null) {
            minioClient = ApplicationContextHolder.getInstance().getBean(CustomizedMinioClient.class);
        }
        return minioClient;
    }

}
