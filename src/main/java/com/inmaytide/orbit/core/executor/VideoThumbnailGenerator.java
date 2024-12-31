package com.inmaytide.orbit.core.executor;

import com.inmaytide.orbit.commons.utils.CodecUtils;
import com.inmaytide.orbit.core.configuration.FileUploaderProperties;
import com.inmaytide.orbit.core.consts.FileCategory;
import com.inmaytide.orbit.core.utils.FileUploadUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author inmaytide
 * @since 2024/5/15
 */
public class VideoThumbnailGenerator implements ThumbnailGenerator {

    @Override
    public boolean support(Path file) {
        Objects.requireNonNull(file);
        String extension = FilenameUtils.getExtension(file.getFileName().toString());
        return FileUploadUtils.isFileCategory(FileCategory.VIDEOS, extension);
    }

    @Override
    public Path generate(Path file) throws Exception {
        support(file);
        FileUploaderProperties.Thumbnail configuration = getThumbnailConfiguration();
        Path res = Files.createTempFile(CodecUtils.randomUUID(), "." + configuration.getFormat());
        try (FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file.toFile()); OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage()) {
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
                            .outputQuality(configuration.getQuality())
                            .outputFormat(configuration.getFormat())
                            .asBufferedImage();
                    try (OutputStream os = Files.newOutputStream(res)) {
                        ImageIO.write(thumbnail, configuration.getFormat(), os);
                    }
                    break;
                }
            }
            return res;
        }
    }

}
