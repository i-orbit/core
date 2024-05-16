package com.inmaytide.orbit.core.api;

import com.inmaytide.orbit.commons.domain.SystemProperty;
import com.inmaytide.orbit.core.service.SystemPropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/19
 */
@RestController
@RequestMapping("/api/system/properties")
@Tag(name = "系统属性", description = "针对允许用户根据不同租户配置不同值的系统属性(如: 系统名称、百度地图API_KEY等)")
public class SystemPropertyResource {

    private final SystemPropertyService service;

    public SystemPropertyResource(SystemPropertyService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "查询指定租户有权限查询的系统属性列表", description = "未登陆可查询不需要鉴权的系统属性")
    public List<SystemProperty> all(@RequestParam(required = false) Long tenantId) {
        return service.all(tenantId);
    }

}
