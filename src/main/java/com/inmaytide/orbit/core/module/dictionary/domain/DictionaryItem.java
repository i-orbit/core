package com.inmaytide.orbit.core.module.dictionary.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.inmaytide.orbit.framework.core.domain.TombstoneEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author inmaytide
 * @since 2026/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dictionary_item")
public class DictionaryItem extends TombstoneEntity {

    /**
     * 所属租户ID
     */
    private String tenantId;

    /**
     * 所属分类ID
     */
    private String categoryId;

    /**
     * 父级字典项ID
     */
    private String parentId;

    /**
     * 字典项编码
     */
    private String code;

    /**
     * 字典项名称
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

}
