package com.inmaytide.orbit.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.inmaytide.orbit.commons.constants.Is;
import com.inmaytide.orbit.commons.domain.pattern.TombstoneEntity;
import com.inmaytide.orbit.core.consts.AppPlatform;
import com.inmaytide.orbit.core.consts.AppState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
@Schema(title = "APP版本信息")
public class AppVersion extends TombstoneEntity {

    @NotNull
    @Schema(title = "APP系统平台")
    private AppPlatform platform;

    @TableField(exist = false)
    @Schema(title = "APP系统平台中文描述")
    private String platformName;

    @NotBlank
    @Schema(title = "APP名称")
    private String name;

    @NotBlank
    @Schema(title = "版本号")
    private String versionNumber;

    @Schema(title = "版本介绍")
    private String description;

    @Schema(title = "是否强制升级")
    private Is forceUpgrade = Is.N;

    @NotNull
    @Schema(title = "APP文件对应文件基本信息唯一标识")
    private Long fileId;

    @Schema(title = "版本状态")
    private AppState state;

    @Schema(title = "状态时间")
    private Instant stateTime;

    @TableField(exist = false)
    @Schema(title = "版本状态描述")
    private String stateName;

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Is getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(Is forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public AppState getState() {
        return state;
    }

    public void setState(AppState state) {
        this.state = state;
    }

    public Instant getStateTime() {
        return stateTime;
    }

    public void setStateTime(Instant stateTime) {
        this.stateTime = stateTime;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
