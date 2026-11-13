package com.inmaytide.orbit.core.module.operationlog.service;

import com.inmaytide.orbit.core.module.operationlog.domain.OperationLog;
import com.inmaytide.orbit.core.module.operationlog.domain.OperationLogQuery;
import com.inmaytide.orbit.core.module.operationlog.domain.OperationLogVO;
import com.inmaytide.orbit.framework.core.domain.dto.PageResult;

import java.util.List;

/**
 * @author inmaytide
 * @since 2026/11/12
 */
public interface OperationLogService {

    String RESOURCE_TYPE = "OperationLog";

    void create(OperationLog inst);

    void batchCreate(List<OperationLog> instances);

    PageResult<OperationLogVO> listPage(OperationLogQuery query);

}
