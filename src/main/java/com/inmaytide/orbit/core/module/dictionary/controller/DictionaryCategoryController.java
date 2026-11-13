package com.inmaytide.orbit.core.module.dictionary.controller;

import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategoryVO;
import com.inmaytide.orbit.core.module.dictionary.service.DictionaryCategoryService;
import com.inmaytide.orbit.core.module.dictionary.service.DictionaryItemService;
import com.inmaytide.orbit.framework.core.domain.dto.ResponseWrapper;
import com.inmaytide.orbit.framework.log.OperationType;
import com.inmaytide.orbit.framework.log.annotation.OperationLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
@RestController
@Tag(name = "数据字典分类")
@RequestMapping("/api/dictionaries/categories")
public class DictionaryCategoryController {

    private final DictionaryCategoryService service;

    public DictionaryCategoryController(DictionaryCategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "查询所有可用的数据字典分类", description = "超级管理员可返回禁用的数据字典分类")
    @OperationLog(operationType = OperationType.QUERY, resourceType = DictionaryItemService.RESOURCE_TYPE)
    public ResponseWrapper<List<DictionaryCategoryVO>> listAll() {
        return ResponseWrapper.success(service.listAll());
    }

    @GetMapping("{code}")
    @Operation(summary = "查询指定数据字典分类(编码)详细信息")
    @OperationLog(operationType = OperationType.QUERY, resourceType = DictionaryItemService.RESOURCE_TYPE, retainRequestBody = true)
    public ResponseWrapper<DictionaryCategoryVO> getByCode(@PathVariable String code) {
        return service.getByCode(code)
                .map(ResponseWrapper::success)
                .orElseGet(() -> ResponseWrapper.failure(404, "dictionaryCategory_notFound", "未找到编码为[%s]的数据字典分类".formatted(code)));
    }

}
