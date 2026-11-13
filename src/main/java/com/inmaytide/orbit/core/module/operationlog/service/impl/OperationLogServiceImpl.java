package com.inmaytide.orbit.core.module.operationlog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.inmaytide.orbit.core.module.operationlog.converter.OperationLogConverter;
import com.inmaytide.orbit.core.module.operationlog.domain.OperationLog;
import com.inmaytide.orbit.core.module.operationlog.domain.OperationLogQuery;
import com.inmaytide.orbit.core.module.operationlog.domain.OperationLogVO;
import com.inmaytide.orbit.core.module.operationlog.repository.OperationLogRepository;
import com.inmaytide.orbit.core.module.operationlog.service.OperationLogService;
import com.inmaytide.orbit.framework.clients.module.iam.api.UserService;
import com.inmaytide.orbit.framework.core.domain.dto.PageResult;
import com.inmaytide.orbit.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * @author inmaytide
 * @since 2026/11/12
 */
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogRepository repository;

    private final OperationLogConverter converter;

    private final UserService userService;

    @Override
    public void create(OperationLog inst) {
        inst.setCreatedAt(Instant.now());
        repository.insert(inst);
    }

    @Override
    public void batchCreate(List<OperationLog> instances) {
        final Instant now = Instant.now();
        instances.forEach(e -> e.setCreatedAt(now));
        repository.insert(instances);
    }

    @Override
    public PageResult<OperationLogVO> listPage(OperationLogQuery query) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        SecurityUtils.getCurrentAuthenticatedUser().ifPresent(user -> wrapper.eq(OperationLog::getTenantId, user.getTenantId()));
        wrapper.orderByDesc(OperationLog::getOperatedAt);
        PageDTO<OperationLog> page = repository.selectPage(new PageDTO<>(query.getPageNumber(), query.getPageSize()), wrapper);

        Map<String, String> usernames = userService.findNamesByIds(page.getRecords().stream().map(OperationLog::getOperatedBy).toList());
        PageResult<OperationLogVO> res = new PageResult<>();
        res.setPageNumber(query.getPageNumber());
        res.setPageSize(query.getPageSize());
        res.setTotal(page.getTotal());
        res.setElements(page.getRecords().stream().map(e -> converter.toVO(e, usernames.get(e.getOperatedBy()))).toList());
        return res;
    }
}
