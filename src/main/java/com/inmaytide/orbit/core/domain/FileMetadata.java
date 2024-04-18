package com.inmaytide.orbit.core.domain;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.inmaytide.orbit.commons.constants.Bool;
import com.inmaytide.orbit.commons.domain.pattern.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
@Schema(title = "文件库文件元数据")
public class FileMetadata extends Entity {

    @NotBlank
    @Schema(title = "文件名称")
    private String name;

    @Schema(title = "文件类型(后缀名)")
    private String extension;

    @Schema(title = "文件大小(bytes)")
    private Long size;

    @Schema(title = "文件存储地址")
    private String address;

    @Schema(title = "图片/视频文件缩略图存储地址")
    private String thumbnailAddress;

    @Schema(title = "文件sha256值")
    private String sha256;

    @Schema(title = "验证标识")
    private Bool verified;

    @Schema(title = "删除标识")
    @TableLogic(value = "'N'", delval = "'Y'")
    private Bool deleted;

    @Schema(title = "删除时间")
    private Instant deleteTime;

    public static FileMetadataBuilder builder() {
        return new FileMetadataBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumbnailAddress() {
        return thumbnailAddress;
    }

    public void setThumbnailAddress(String thumbnailAddress) {
        this.thumbnailAddress = thumbnailAddress;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Bool getVerified() {
        return verified;
    }

    public void setVerified(Bool verified) {
        this.verified = verified;
    }

    public Bool getDeleted() {
        return deleted;
    }

    public void setDeleted(Bool deleted) {
        this.deleted = deleted;
    }

    public Instant getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Instant deleteTime) {
        this.deleteTime = deleteTime;
    }

    public static class FileMetadataBuilder {

        private String address;

        private String thumbnailAddress;

        private String filename;

        private long size;

        private String sha256;

        private Bool verified;

        public FileMetadataBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public FileMetadataBuilder address(String address) {
            this.address = address;
            return this;
        }

        public FileMetadataBuilder thumbnailAddress(String thumbnailAddress) {
            this.thumbnailAddress = thumbnailAddress;
            return this;
        }

        public FileMetadataBuilder size(long size) {
            this.size = size;
            return this;
        }

        public FileMetadataBuilder sha256(String sha256) {
            this.sha256 = sha256;
            return this;
        }

        public FileMetadataBuilder verified(Bool verified) {
            this.verified = verified;
            return this;
        }

        public FileMetadata build() {
            if (size <= 0) {
                throw new IllegalArgumentException("size must be greater than 0");
            }
            if (StringUtils.isBlank(filename)) {
                throw new IllegalArgumentException("filename must not be blank");
            }
            if (StringUtils.isBlank(address)) {
                throw new IllegalArgumentException("address must not be blank");
            }
            if (StringUtils.isBlank(sha256)) {
                throw new IllegalArgumentException("sha256 must not be blank");
            }
            FileMetadata entity = new FileMetadata();
            entity.setSha256(sha256);
            entity.setName(FilenameUtils.removeExtension(filename));
            entity.setExtension(FilenameUtils.getExtension(filename));
            entity.setSize(size);
            entity.setAddress(address);
            entity.setThumbnailAddress(thumbnailAddress);
            entity.setVerified(verified == null ? Bool.N : verified);
            return entity;
        }
    }
}
