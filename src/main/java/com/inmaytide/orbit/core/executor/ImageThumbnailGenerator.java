package com.inmaytide.orbit.core.executor;

import com.inmaytide.orbit.commons.utils.CodecUtils;
import com.inmaytide.orbit.core.configuration.FileUploaderProperties;
import com.inmaytide.orbit.core.consts.FileCategory;
import com.inmaytide.orbit.core.utils.FileUploadUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * @author inmaytide
 * @since 2024/5/15
 */
public class ImageThumbnailGenerator implements ThumbnailGenerator {

    @Override
    public boolean support(Path file) {
        Objects.requireNonNull(file);
        String extension = FilenameUtils.getExtension(file.getFileName().toString());
        return FileUploadUtils.isFileCategory(FileCategory.IMAGES, extension);
    }

    @Override
    public Path generate(Path file) throws Exception {
        support(file);
        FileUploaderProperties.Thumbnail configuration = getThumbnailConfiguration();
        Path output = Files.createTempFile(CodecUtils.randomUUID(), "." + configuration.getOutputFormat());
        try (InputStream is = Files.newInputStream(file); OutputStream os = Files.newOutputStream(output, StandardOpenOption.WRITE)) {
            BufferedImage thumbnail = Thumbnails.of(is)
                    .size(configuration.getWidth(), configuration.getHeight())
                    .outputQuality(configuration.getOutputQuality())
                    .outputFormat(configuration.getOutputFormat())
                    .asBufferedImage();
            ImageIO.write(thumbnail, configuration.getOutputFormat(), os);
        }
        return output;
    }
}
