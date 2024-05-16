package com.inmaytide.orbit.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inmaytide.orbit.commons.constants.Bool;
import com.inmaytide.orbit.commons.constants.Constants;
import com.inmaytide.orbit.commons.domain.SystemProperty;
import com.inmaytide.orbit.commons.domain.SystemUser;
import com.inmaytide.orbit.commons.security.SecurityUtils;
import com.inmaytide.orbit.core.mapper.SystemPropertyMapper;
import com.inmaytide.orbit.core.service.SystemPropertyService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public List<SystemProperty> all(Long tenantId) {
        LambdaQueryWrapper<SystemProperty> wrapper = new LambdaQueryWrapper<>();
        if (!SecurityUtils.isAuthorized()) {
            // 未登录用户只能查询前端可访问的指定租户或系统默认租户的系统属性
            wrapper.eq(SystemProperty::getExposed, Bool.Y);
            wrapper.eq(SystemProperty::getAuthenticated, Bool.N);
            wrapper.eq(SystemProperty::getTenantId, tenantId == null ? Constants.Markers.NON_TENANT_ID : tenantId);
        } else {
            SystemUser user = SecurityUtils.getAuthorizedUser();
            tenantId = tenantId == null ? user.getTenant() : tenantId;
            wrapper.eq(SystemProperty::getTenantId, tenantId);
            // 非超级权限只能查询对前端暴露的系统属性
            if (SecurityUtils.isSuperAdministrator() && !SecurityUtils.isTenantAdministrator(tenantId) && !SecurityUtils.isRobot()) {
                wrapper.eq(SystemProperty::getExposed, Bool.Y);
            }
        }
        return propertyMapper.selectList(wrapper);
    }
}
