package com.inmaytide.orbit.core.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author inmaytide
 * @since 2024/4/9
 */
@Schema(title = "分片上传完成后合并文件接口参数")
public class CompleteMultipartUpload implements Serializable {

    @NotBlank
    @Schema(title = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String filename;

    @Schema(title = "上传任务ID")
    private String uploadId;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
}
