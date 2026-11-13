package com.inmaytide.orbit.core.module.dictionary.domain;

import com.inmaytide.orbit.framework.core.domain.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author inmaytide
 * @since 2026/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "数据字典项")
public class DictionaryItemVO extends Entity {

    @Schema(description = "所属租户ID, 为空表示系统通用")
    private String tenantId;

    @Schema(description = "所属分类编码")
    private String categoryCode;

    @Schema(description = "父级字典项")
    private String parentId;

    @Schema(description = "字典项编码")
    private String code;

    @Schema(description = "字典项名称")
    private String name;

    @Schema(description = "字典项说明")
    private String description;

    @Schema(description = "排序字段")
    private Integer displayOrder;

    @Schema(description = "启用状态")
    private Integer status;

    @Schema(description = "树节点路径")
    private String path;

}
