package com.inmaytide.orbit.core.module.dictionary.domain;

import com.inmaytide.orbit.framework.core.domain.dto.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
@Data
@Schema(description = "数据字典分类", name = "数据字典分类")
public class DictionaryCategoryVO {

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

    @Schema(description = "数据字典项树(查询分类列表时该字段返回NULL)")
    private List<TreeNode<DictionaryItemVO>> items;

}
