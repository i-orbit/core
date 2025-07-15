package com.inmaytide.orbit.core.utils;

import com.inmaytide.orbit.commons.utils.ApplicationContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * MinIO 工具类，封装常用的路径拼接、提取逻辑和客户端懒加载。
 * <p>
 * 提供统一的对象路径处理方式，规范地址格式，避免路径拼接错误。
 * </p>
 *
 * <p><strong>For example：</strong></p>
 * <pre>{@code
 *     String address = MinioUtils.getAddress("my-bucket", "folder", "file.txt");
 *     String bucket = MinioUtils.getBucket(address);
 *     String objectName = MinioUtils.getObjectName(address);
 * }</pre>
 *
 * @author inmaytide
 * @since 2024/04/11
 */
public final class MinioUtils {

    private static CustomizedMinioClient minioClient;

    private MinioUtils() {

    }

    /**
     * 构建对象完整路径（地址），格式：bucket/objectName
     */
    public static String getAddress(String bucket, String folder, String filename) {
        return getAddress(bucket, getObjectName(folder, filename));
    }

    /**
     * 构建地址，格式：bucket/objectName
     */
    public static String getAddress(String bucket, String objectName) {
        return bucket + "/" + normalizePath(objectName);
    }

    /**
     * 构建 MinIO 对象名称（即对象路径），格式：folder/filename
     */
    public static String getObjectName(String folder, String filename) {
        return normalizePath(folder) + "/" + normalizePath(filename);
    }

    /**
     * 提取 bucket（第一个路径段）
     */
    public static String getBucket(String address) {
        String normalized = normalizePath(address);
        int index = normalized.indexOf('/');
        return index == -1 ? normalized : normalized.substring(0, index);
    }

    /**
     * 提取 objectName（去掉 bucket）
     */
    public static String getObjectName(String address) {
        String normalized = normalizePath(address);
        int index = normalized.indexOf('/');
        return index == -1 ? "" : normalized.substring(index + 1);
    }

    public static CustomizedMinioClient getMinioClient() {
        if (minioClient == null) {
            minioClient = ApplicationContextHolder.getInstance().getBean(CustomizedMinioClient.class);
        }
        return minioClient;
    }

    /**
     * 清洗路径前后 / 分隔符
     */
    private static String normalizePath(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        return StringUtils.strip(path, "/\\");
    }

}
