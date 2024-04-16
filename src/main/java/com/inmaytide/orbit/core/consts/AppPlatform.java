package com.inmaytide.orbit.core.consts;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
public enum AppPlatform {

    IOS("苹果iOS"),
    ANDROID("安卓OS"),
    HARMONY("华为鸿蒙OS");

    private final String description;

    AppPlatform(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
