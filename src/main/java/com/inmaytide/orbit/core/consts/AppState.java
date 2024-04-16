package com.inmaytide.orbit.core.consts;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
public enum AppState {

    ENABLED("已启用"),

    DISABLED("已禁用");

    private final String description;

    AppState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
