package com.inmaytide.orbit.core.service.dto;

import com.inmaytide.orbit.commons.utils.DatetimeUtils;
import com.inmaytide.orbit.core.executor.FileUploader;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

/**
 * @author inmaytide
 * @since 2024/4/8
 */
@Schema(title = "请求创建分片上传接口参数")
public class CreateMultipartUpload {

    @NotBlank
    @Schema(title = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String filename;

    @NotBlank
    @Schema(title = "文件的sha256值", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sha256;

    @Min(1)
    @NotNull
    @Schema(title = "分片数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer partCount;

    @Schema(title = "存储MinIO桶", defaultValue = FileUploader.FILE_LIBRARY_BUCKET)
    private String bucket = FileUploader.FILE_LIBRARY_BUCKET;

    @Schema(title = "存储路径", defaultValue = "当前日期(如: 20240408)")
    private String path;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Integer getPartCount() {
        return partCount;
    }

    public void setPartCount(Integer partCount) {
        this.partCount = partCount;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPath() {
        if (StringUtils.isBlank(path)) {
            this.path = DatetimeUtils.formatDateWithoutJoiner(Instant.now());
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
