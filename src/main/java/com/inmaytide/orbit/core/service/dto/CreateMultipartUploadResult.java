package com.inmaytide.orbit.core.service.dto;

import com.inmaytide.orbit.core.domain.FileMetadata;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/8
 */
@Schema(title = "请求创建分片上传接口返回对象")
public class CreateMultipartUploadResult {

    @Schema(title = "已存在的文件元数据", description = "通过文件SHA256对比文件是否已存在文件库，如果已存在直接返回对应的文件信息; 实现秒传")
    private FileMetadata fileMetadata;

    @Schema(title = "存储文件名称")
    private String objectName;

    @Schema(title = "实际文件名")
    private String originalFilename;

    @Schema(title = "文件SHA265值")
    private String sha265;

    @Schema(title = "桶")
    private String bucket;

    @Schema(title = "上传任务ID")
    private String uploadId;

    @Schema(title = "分片列表")
    private final List<Part> parts;

    public CreateMultipartUploadResult(int partCount) {
        this.parts = new ArrayList<>(partCount);
    }

    public FileMetadata getFileMetadata() {
        return fileMetadata;
    }

    public void setFileMetadata(FileMetadata fileMetadata) {
        this.fileMetadata = fileMetadata;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getSha265() {
        return sha265;
    }

    public void setSha265(String sha265) {
        this.sha265 = sha265;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void addPart(Part part) {
        this.parts.add(part);
    }

    public void addPart(Integer number, String uploadAddress) {
        addPart(new Part(number, uploadAddress));
    }

    public static class Part {

        @Schema(title = "分片编号")
        private Integer number;

        @Schema(title = "分片内容上传地址")
        private String uploadAddress;

        public Part(Integer number, String uploadAddress) {
            this.number = number;
            this.uploadAddress = uploadAddress;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getUploadAddress() {
            return uploadAddress;
        }

        public void setUploadAddress(String uploadAddress) {
            this.uploadAddress = uploadAddress;
        }
    }

}
