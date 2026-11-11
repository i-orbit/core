package com.inmaytide.orbit.core.module.dictionary.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.Instant;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
@Data
@TableName("dictionary_category")
public class DictionaryCategory {

    /**
     * 主键ID（UUIDv7，无横杠）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

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

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)

    private Instant createdAt;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Instant updatedAt;

    /**
     * 乐观锁版本
     */
    @Version
    private Integer version;

    /**
     * 逻辑删除：0=未删除，1=已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

}
