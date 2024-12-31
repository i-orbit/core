package com.inmaytide.orbit.core.configuration;

import com.inmaytide.orbit.commons.utils.CommonUtils;
import com.inmaytide.orbit.core.consts.FileCategory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author inmaytide
 * @since 2024/4/8
 */
public class FileUploaderProperties {

    private Long singleFileMaximumSize;

    private Extensions allowedExtensions;

    private Thumbnail thumbnail;

    public Long getSingleFileMaximumSize() {
        return singleFileMaximumSize;
    }

    public void setSingleFileMaximumSize(Long singleFileMaximumSize) {
        this.singleFileMaximumSize = singleFileMaximumSize;
    }

    public Extensions getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(Extensions allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
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

        private float quality = 0.75f;

        private String format = "jpg";

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

        public float getQuality() {
            return quality;
        }

        public void setQuality(float quality) {
            this.quality = quality;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }

    public static class Extensions {

        private Set<String> images;

        private Set<String> videos;

        private Set<String> documents;

        private Set<String> archives;

        private Set<String> others;

        private Set<String> apps;

        private void set(FileCategory category, String value) {
            Field f = ReflectionUtils.findFieldIgnoreCase(this.getClass(), category.name());
            Objects.requireNonNull(f);
            f.setAccessible(true);
            ReflectionUtils.setField(f, this, StringUtils.isBlank(value) ? Collections.emptySet() : new HashSet<>(CommonUtils.splitByCommas(value)));
        }

        public Set<String> get(FileCategory category) {
            Field f = ReflectionUtils.findFieldIgnoreCase(this.getClass(), category.name());
            Objects.requireNonNull(f);
            f.setAccessible(true);
            Object value = ReflectionUtils.getField(f, this);
            if (value instanceof Set<?>) {
                return (Set<String>) value;
            }
            return Collections.emptySet();
        }

        public Set<String> all() {
            return Stream.of(images, videos, documents, archives, others, apps)
                    .filter(CollectionUtils::isNotEmpty)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }

        public Set<String> getImages() {
            return images;
        }

        public void setImages(String images) {
            set(FileCategory.IMAGES, images);
        }

        public Set<String> getVideos() {
            return videos;
        }

        public void setVideos(String videos) {
            set(FileCategory.VIDEOS, videos);
        }

        public Set<String> getDocuments() {
            return documents;
        }

        public void setDocuments(String documents) {
            set(FileCategory.DOCUMENTS, documents);
        }

        public Set<String> getArchives() {
            return archives;
        }

        public void setArchives(String archives) {
            set(FileCategory.ARCHIVES, archives);
        }

        public Set<String> getApps() {
            return apps;
        }

        public void setApps(String apps) {
            set(FileCategory.APPS, apps);
        }

        public Set<String> getOthers() {
            return others;
        }

        public void setOthers(String others) {
            set(FileCategory.OTHERS, others);
        }
    }

}
