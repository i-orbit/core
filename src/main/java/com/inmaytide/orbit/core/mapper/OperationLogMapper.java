package com.inmaytide.orbit.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inmaytide.orbit.commons.log.domain.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}