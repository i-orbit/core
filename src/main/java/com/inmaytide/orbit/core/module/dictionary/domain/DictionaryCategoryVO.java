package com.inmaytide.orbit.core.module.dictionary.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
@Data
@Schema(description = "数据字典分类")
public class DictionaryCategoryVO {

    @Schema(description = "主键标识")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "上级分类主键标识, 根目录为NULL")
    private String parentId;

    @Schema(description = "分类编码")
    private String code;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类描述")
    private String description;

    @Schema(description = "排序字段")
    private Integer displayOrder;

    @Schema(description = "启用状态(1-启用;0-禁用;)")
    private Integer status;

    @Schema(description = "树节点路径")
    private String path;

    @Schema(description = "字典项允许最大层级")
    private Integer maxAllowedLevel;

}
