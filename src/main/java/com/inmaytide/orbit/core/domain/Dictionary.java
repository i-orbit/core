package com.inmaytide.orbit.core.domain;

import com.inmaytide.orbit.commons.constants.Sharing;
import com.inmaytide.orbit.commons.constants.Source;
import com.inmaytide.orbit.commons.domain.pattern.SharingEntity;
import com.inmaytide.orbit.commons.domain.pattern.TombstoneEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
@Schema(title = "数据字典")
public class Dictionary extends TombstoneEntity implements SharingEntity, Comparable<Dictionary> {

    @Schema(title = "字典编码", description = "分类编码+字典编码全局唯一")
    private String code;

    @Schema(title = "字典名称")
    private String name;

    @Schema(title = "分类编码")
    private String category;

    @Schema(title = "数据来源", description = "用于区分字典是系统自建或用户添加，其中系统自建不允许用户修改")
    private Source source;

    @Schema(title = "上级字典编码", description = "根目录值为ROOT")
    private String parent;

    @Schema(title = "数据字典作为下拉选项时的VALUE值, 为空时取字典编码值")
    private String optionValue;

    @Schema(title = "字典共享级别")
    private Sharing sharing;

    @Schema(title = "排序字段")
    private Integer sequence;

    @Schema(title = "所属租户")
    private Long tenant;

    @Schema(title = "所属区域")
    private Long area;

    @Schema(title = "所属组织")
    private Long organization;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Sharing getSharing() {
        return sharing;
    }

    public void setSharing(Sharing sharing) {
        this.sharing = sharing;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public Long getArea() {
        return area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    @Override
    public int compareTo(@NotNull Dictionary other) {
        return Integer.compare(this.getSequence(), Objects.requireNonNull(other).getSequence());
    }
}
