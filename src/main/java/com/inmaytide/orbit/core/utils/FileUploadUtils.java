package com.inmaytide.orbit.core.utils;

import com.inmaytide.orbit.commons.utils.ApplicationContextHolder;
import com.inmaytide.orbit.commons.utils.CodecUtils;
import com.inmaytide.orbit.core.configuration.ApplicationProperties;
import com.inmaytide.orbit.core.configuration.FileUploaderProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * 文件处理的一些工具方法
 *
 * @author inmaytide
 * @since 2022/8/25
 */
public class FileUploadUtils {

    private static FileUploaderProperties.SupportExtensions supportExtensions;

    private static CustomizedMinioClient minioClient;

    private FileUploadUtils() {

    }

    public static void delete(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean support(String extension) {
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        final String forMatching = StringUtils.startsWith(extension, ".") ? extension.substring(1) : extension;
        return Stream.of(getSupportExtensions().getAllSupportedExtensions())
                .flatMap(Collection::stream)
                .anyMatch(ext -> ext.equalsIgnoreCase(forMatching));
    }

    public static boolean isImage(String extension) {
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        final String forMatching = StringUtils.startsWith(extension, ".") ? extension.substring(1) : extension;
        return getSupportExtensions().getImages().stream().anyMatch(ext -> ext.equalsIgnoreCase(forMatching));
    }

    public static boolean isVideo(String extension) {
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        final String forMatching = StringUtils.startsWith(extension, ".") ? extension.substring(1) : extension;
        return getSupportExtensions().getVideos().stream().anyMatch(ext -> ext.equalsIgnoreCase(forMatching));
    }

    public static boolean isApp(String extension) {
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        final String forMatching = StringUtils.startsWith(extension, ".") ? extension.substring(1) : extension;
        return getSupportExtensions().getApps().stream().anyMatch(ext -> ext.equalsIgnoreCase(forMatching));
    }

    /**
     * 检查MinIO中是否已存在写入目标桶，如果不存在则新建该桶
     */
    public static void createBucketIfNotExist(String bucket) throws Exception {
        if (getMinioClient().bucketExists(BucketExistsArgs.builder().bucket(bucket).build()).get()) {
            return;
        }
        getMinioClient().makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }

    public static void upload(String bucket, String address, Path file) throws Exception {
        createBucketIfNotExist(bucket);
        PutObjectArgs args = PutObjectArgs.builder().bucket(bucket)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .object(address)
                .stream(Files.newInputStream(file), Files.size(file), -1)
                .build();
        getMinioClient().putObject(args);
    }

    public static String getFileSHA256(Path file) {
        try (BufferedInputStream is = new BufferedInputStream(Files.newInputStream(file))) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[16384];
            int sizeRead;
            while ((sizeRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, sizeRead);
            }
            return toHexStr(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHexStr(byte[] data) {
        StringBuilder value = new StringBuilder(data.length * 2);
        for (byte b : data) {
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) {
                value.append("0");
            } else if (hex.length() == 8) {
                hex = hex.substring(6);
            }
            value.append(hex);
        }
        return value.toString().toLowerCase(Locale.getDefault());
    }

    public static long getSingleFileMaximumSize() {
        return ApplicationContextHolder.getInstance()
                .getBean(ApplicationProperties.class)
                .getFileUploader()
                .getSingleFileMaximumSize();
    }

    public static FileUploaderProperties.SupportExtensions getSupportExtensions() {
        if (supportExtensions == null) {
            supportExtensions = ApplicationContextHolder.getInstance()
                    .getBean(ApplicationProperties.class)
                    .getFileUploader()
                    .getSupportExtensions();
        }
        return supportExtensions;
    }

    public static FileUploaderProperties.Thumbnail getThumbnail() {
        return ApplicationContextHolder.getInstance()
                .getBean(ApplicationProperties.class)
                .getFileUploader()
                .getThumbnail();
    }

    public static CustomizedMinioClient getMinioClient() {
        if (minioClient == null) {
            minioClient = ApplicationContextHolder.getInstance().getBean(CustomizedMinioClient.class);
        }
        return minioClient;
    }

    public static Path generateThumbnail(Path file) {
        String extension = FilenameUtils.getExtension(file.getFileName().toString());
        if (isImage(extension)) {
            return generateThumbnailForImage(file);
        }
        if (isVideo(extension)) {
            return generateThumbnailForVideo(file);
        }
        throw new IllegalArgumentException("unsupported extension: " + extension);
    }

    private static Path generateThumbnailForVideo(Path file) {
        FileUploaderProperties.Thumbnail configuration = getThumbnail();
        try (FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file.toFile()); OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage()) {
            Path res = Files.createTempFile(CodecUtils.randomUUID(), configuration.getOutputFormat());
            grabber.start();
            String rotate = grabber.getVideoMetadata("rotate");
            int len = grabber.getLengthInFrames();
            // 取中间帧
            int index = len / 2;
            for (int i = 0; i < len; i++) {
                Frame frame = grabber.grabImage();
                if (i == index) {
                    if (rotate != null && rotate.length() > 1) {
                        IplImage src = converter.convert(frame);
                        try (IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels())) {
                            opencv_core.cvTranspose(src, img);
                            opencv_core.cvFlip(img, img, Integer.parseInt(rotate));
                            frame = converter.convert(img);
                        }
                    }
                    BufferedImage image = Java2DFrameUtils.toBufferedImage(frame);
                    BufferedImage thumbnail = Thumbnails.of(image)
                            .size(configuration.getWidth(), configuration.getHeight())
                            .outputQuality(configuration.getOutputQuality())
                            .outputFormat(configuration.getOutputFormat())
                            .asBufferedImage();
                    try (OutputStream os = Files.newOutputStream(res)) {
                        ImageIO.write(thumbnail, configuration.getOutputFormat(), os);
                    }
                    break;
                }
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Path generateThumbnailForImage(Path file) {
        FileUploaderProperties.Thumbnail configuration = getThumbnail();
        try {
            Path res = Files.createTempFile(CodecUtils.randomUUID(), configuration.getOutputFormat());
            try (InputStream is = Files.newInputStream(file); OutputStream os = Files.newOutputStream(res, StandardOpenOption.CREATE_NEW)) {
                BufferedImage thumbnail = Thumbnails.of(is)
                        .size(configuration.getWidth(), configuration.getHeight())
                        .outputQuality(configuration.getOutputQuality())
                        .outputFormat(configuration.getOutputFormat())
                        .asBufferedImage();
                ImageIO.write(thumbnail, configuration.getOutputFormat(), os);
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
