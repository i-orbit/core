package com.inmaytide.orbit.core.domain;

import com.inmaytide.orbit.commons.domain.pattern.TombstoneEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
@Schema(title = "文件库文件引用记录")
public class FileAssociation extends TombstoneEntity {

    @NotNull
    @Schema(title = "文件库文件唯一标识")
    private Long fileId;

    @NotNull
    @Schema(title = "业务标识", description = "系统业务分类-数据字典编码")
    private String business;

    @NotNull
    @Schema(title = "引用文件业务对象唯一标识")
    private Long businessDataId;

    @Schema(title = "引用文件业务对象描述(如工单编号、视频名称等)")
    private String businessDataDescription;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Long getBusinessDataId() {
        return businessDataId;
    }

    public void setBusinessDataId(Long businessDataId) {
        this.businessDataId = businessDataId;
    }

    public String getBusinessDataDescription() {
        return businessDataDescription;
    }

    public void setBusinessDataDescription(String businessDataDescription) {
        this.businessDataDescription = businessDataDescription;
    }
}
