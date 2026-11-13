package com.inmaytide.orbit.core.module.operationlog.converter;

import com.inmaytide.orbit.core.module.operationlog.domain.OperationLog;
import com.inmaytide.orbit.core.module.operationlog.domain.OperationLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author inmaytide
 * @since 2026/11/13
 */
@Mapper(componentModel = "spring")
public interface OperationLogConverter {

    @Mapping(target = "operatedByName", source = "operatedByName")
    OperationLogVO toVO(OperationLog inst, String operatedByName);

}
