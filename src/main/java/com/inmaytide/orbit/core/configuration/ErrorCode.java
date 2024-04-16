package com.inmaytide.orbit.core.configuration;

/**
 * @author inmaytide
 * @since 2023/5/6
 */
public enum ErrorCode implements com.inmaytide.exception.web.domain.ErrorCode {

    E_0x00300001("0x00300001", "文件超过单个文件最大限制"),
    E_0x00300002("0x00300002", "上传的文件大小为空"),
    E_0x00300003("0x00300003", "不支持的文件类型"),
    E_0x00300004("0x00300004", "分片上传对应的任务信息不存在或已过期"),
    E_0x00300005("0x00300005", "合并分片时发生错误"),
    E_0x00300006("0x00300006", "获取文件基本信息失败"),
    E_0x00300007("0x00300007", "ID 为 {0} 的文件不存在"),
    E_0x00300008("0x00300008", "下载文件时发生错误"),
    E_0x00300009("0x00300009", "没有找到 Code 为 {0} 的数据字典分类");

    private final String value;

    private final String description;

    ErrorCode(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }
}
