package com.inmaytide.orbit.core.configuration;

import com.inmaytide.orbit.commons.utils.CommonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author inmaytide
 * @since 2024/4/8
 */
public class FileUploaderProperties {

    private Long singleFileMaximumSize;

    private SupportExtensions supportExtensions;

    private Thumbnail thumbnail;

    public Long getSingleFileMaximumSize() {
        return singleFileMaximumSize;
    }

    public void setSingleFileMaximumSize(Long singleFileMaximumSize) {
        this.singleFileMaximumSize = singleFileMaximumSize;
    }

    public SupportExtensions getSupportExtensions() {
        return supportExtensions;
    }

    public void setSupportExtensions(SupportExtensions supportExtensions) {
        this.supportExtensions = supportExtensions;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public static class Thumbnail {

        private Integer width = 150;

        private Integer height = 150;

        private float outputQuality = 0.75f;

        private String outputFormat = "jpg";

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public float getOutputQuality() {
            return outputQuality;
        }

        public void setOutputQuality(float outputQuality) {
            this.outputQuality = outputQuality;
        }

        public String getOutputFormat() {
            return StringUtils.lowerCase(outputFormat);
        }

        public void setOutputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
        }
    }

    public static class SupportExtensions {

        private static final Pattern COMMA = Pattern.compile(",");

        private Set<String> images;

        private Set<String> videos;

        private Set<String> documents;

        private Set<String> archives;

        private Set<String> others;

        private Set<String> apps;

        private void set(String field, String value) {
            Field f = ReflectionUtils.findField(this.getClass(), field);
            Objects.requireNonNull(f);
            f.setAccessible(true);
            ReflectionUtils.setField(f, this, StringUtils.isBlank(value) ? Collections.emptySet() : new HashSet<>(CommonUtils.splitByCommas(value)));
        }

        public Set<String> getAllSupportedExtensions() {
            return Stream.of(images, videos, documents, archives, others, apps)
                    .filter(CollectionUtils::isNotEmpty)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }

        public Set<String> getApps() {
            return apps;
        }

        public void setApps(String apps) {
            set("apps", apps);
        }

        public Set<String> getImages() {
            return images;
        }

        public void setImages(String images) {
            set("images", images);
        }

        public Set<String> getVideos() {
            return videos;
        }

        public void setVideos(String videos) {
            set("videos", videos);
        }

        public Set<String> getOthers() {
            return others;
        }

        public void setOthers(String others) {
            set("others", others);
        }

        public Set<String> getDocuments() {
            return documents;
        }

        public void setDocuments(String documents) {
            set("documents", documents);
        }

        public Set<String> getArchives() {
            return archives;
        }

        public void setArchives(String archives) {
            set("archives", archives);
        }
    }

}
