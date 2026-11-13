package com.inmaytide.orbit.core.module.operationlog.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inmaytide.orbit.core.module.operationlog.domain.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author inmaytide
 * @since 2026/11/12
 */
@Mapper
public interface OperationLogRepository extends BaseMapper<OperationLog> {
}
