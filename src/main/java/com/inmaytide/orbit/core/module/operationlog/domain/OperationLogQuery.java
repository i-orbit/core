package com.inmaytide.orbit.core.module.operationlog.domain;

import com.inmaytide.orbit.framework.core.domain.dto.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author inmaytide
 * @since 2026/11/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "操作日志查询参数")
public class OperationLogQuery extends PageQuery {
}
