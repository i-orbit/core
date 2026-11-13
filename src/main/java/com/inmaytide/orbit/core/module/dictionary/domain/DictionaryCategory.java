package com.inmaytide.orbit.core.module.dictionary.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.inmaytide.orbit.framework.core.domain.TombstoneEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dictionary_category")
public class DictionaryCategory extends TombstoneEntity {

    /**
     * 父级分类ID
     */
    private String parentId;

    /**
     * 字典分类编码
     */
    private String code;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序号
     */
    private Integer displayOrder;

    /**
     * 状态：0=禁用，1=启用
     */
    private Integer status;

    /**
     * 物化路径
     */
    private String path;

    /**
     * 字典允许最大层级，-1 表示不限制
     */
    private Integer maxAllowedLevel;

}
