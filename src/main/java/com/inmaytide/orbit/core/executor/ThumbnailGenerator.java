package com.inmaytide.orbit.core.executor;

import com.inmaytide.orbit.commons.utils.ApplicationContextHolder;
import com.inmaytide.orbit.core.configuration.ApplicationProperties;
import com.inmaytide.orbit.core.configuration.FileUploaderProperties;

import java.nio.file.Path;
import java.util.List;

/**
 * @author inmaytide
 * @since 2024/5/15
 */
public interface ThumbnailGenerator {

    List<ThumbnailGenerator> ALL_GENERATOR_INSTANCES = List.of(new ImageThumbnailGenerator(), new VideoThumbnailGenerator());

    boolean support(Path file);

    Path generate(Path file) throws Exception;

    default FileUploaderProperties.Thumbnail getThumbnailConfiguration() {
        return ApplicationContextHolder.getInstance()
                .getBean(ApplicationProperties.class)
                .getFileUploader()
                .getThumbnail();
    }

    default String getOutputNameSuffix() {
        FileUploaderProperties.Thumbnail configuration = getThumbnailConfiguration();
        return "_%dx%d".formatted(configuration.getWidth(), configuration.getHeight()) + "." + configuration.getOutputFormat();
    }
}
