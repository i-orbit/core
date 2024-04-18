package com.inmaytide.orbit.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.inmaytide.orbit.commons.constants.Bool;
import com.inmaytide.orbit.commons.domain.dto.result.TreeNode;
import com.inmaytide.orbit.commons.domain.pattern.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
@Schema(title = "数据字典分类", description = "不提供管理(增删改)接口, 只提供查询列表接口")
public class DictionaryCategory extends Entity {

    @Schema(title = "类别编码", description = "全局唯一")
    private String code;

    @Schema(title = "类别名称")
    private String name;

    @Schema(title = "对应数据字典树最大层级, 值为-1时表示无限制")
    private Integer maxDepth;

    @Schema(title = "对应数据字典是否允许修改")
    private Bool allowChanges;

    @Schema(title = "数据字典类别是否禁用", description = "主要用于有些项目对应的业务功能不需要，可禁用对应的字典类别")
    private Bool disabled;

    @Schema(title = "排序字段")
    private Integer sequence;

    @TableField(exist = false)
    @Schema(title = "对应的数据字典列表")
    private List<TreeNode<Dictionary>> dictionaries;

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

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Bool getAllowChanges() {
        return allowChanges;
    }

    public void setAllowChanges(Bool allowChanges) {
        this.allowChanges = allowChanges;
    }

    public Bool getDisabled() {
        return disabled;
    }

    public void setDisabled(Bool disabled) {
        this.disabled = disabled;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<TreeNode<Dictionary>> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(List<TreeNode<Dictionary>> dictionaries) {
        this.dictionaries = dictionaries;
    }
}
