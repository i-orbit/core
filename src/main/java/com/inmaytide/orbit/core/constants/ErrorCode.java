package com.inmaytide.orbit.core.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * core 服务业务错误码。
 *
 * @author inmaytide
 * @since 2026/11/26
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode implements com.inmaytide.orbit.framework.core.exception.ErrorCode {

    DICTIONARY_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "dictionary_category_not_found", "未找到指定的数据字典分类");

    private final HttpStatus httpStatus;

    private final String code;

    private final String description;

}
