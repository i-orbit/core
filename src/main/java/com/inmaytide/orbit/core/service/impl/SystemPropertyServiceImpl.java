package com.inmaytide.orbit.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inmaytide.orbit.commons.domain.SystemProperty;
import com.inmaytide.orbit.core.mapper.SystemPropertyMapper;
import com.inmaytide.orbit.core.service.SystemPropertyService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author inmaytide
 * @since 2024/4/15
 */
@Primary
@Service
public class SystemPropertyServiceImpl implements SystemPropertyService {

    private final SystemPropertyMapper propertyMapper;

    public SystemPropertyServiceImpl(SystemPropertyMapper propertyMapper) {
        this.propertyMapper = propertyMapper;
    }

    private SystemProperty create(SystemProperty property) {
        SystemProperty res = SystemProperty.empty(property.getTenantId(), property.getName());
        res.setDescription(property.getDescription());
        propertyMapper.insert(res);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void persist(SystemProperty property) {
        SystemProperty p = get(property.getTenantId(), property.getName()).orElseGet(() -> create(property));
        LambdaUpdateWrapper<SystemProperty> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SystemProperty::getTenantId, p.getTenantId());
        wrapper.eq(SystemProperty::getName, p.getName());
        wrapper.set(SystemProperty::getValue, p.getValue());
        propertyMapper.update(wrapper);
    }

    @Override
    public Optional<SystemProperty> get(Long tenant, String name) {
        LambdaQueryWrapper<SystemProperty> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemProperty::getTenantId, tenant);
        wrapper.eq(SystemProperty::getName, name);
        return Optional.ofNullable(propertyMapper.selectOne(wrapper));
    }
}
