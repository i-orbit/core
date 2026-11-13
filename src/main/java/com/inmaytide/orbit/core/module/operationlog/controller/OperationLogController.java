package com.inmaytide.orbit.core.module.operationlog.controller;

import com.inmaytide.orbit.core.module.operationlog.domain.OperationLogQuery;
import com.inmaytide.orbit.core.module.operationlog.domain.OperationLogVO;
import com.inmaytide.orbit.core.module.operationlog.service.OperationLogService;
import com.inmaytide.orbit.framework.core.domain.dto.PageResult;
import com.inmaytide.orbit.framework.core.domain.dto.ResponseWrapper;
import com.inmaytide.orbit.framework.log.OperationType;
import com.inmaytide.orbit.framework.log.annotation.OperationLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inmaytide
 * @since 2026/11/13
 */
@RestController
@Tag(name = "操作日志")
@RequestMapping("/api/operation/logs")
public class OperationLogController {

    private final OperationLogService service;

    public OperationLogController(OperationLogService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "分页查询操作日志列表")
    @OperationLog(operationType = OperationType.QUERY, resourceType = OperationLogService.RESOURCE_TYPE)
    public ResponseWrapper<PageResult<OperationLogVO>> listPage(@ModelAttribute OperationLogQuery query) {
        return ResponseWrapper.success(service.listPage(query));
    }

}
