package com.inmaytide.orbit.core.service;

import com.inmaytide.orbit.commons.domain.SystemProperty;

import java.util.Optional;

/**
 * @author inmaytide
 * @since 2024/4/15
 */
public interface SystemPropertyService {

    void persist(SystemProperty systemProperty);

    Optional<SystemProperty> get(Long tenant, String name);

}
