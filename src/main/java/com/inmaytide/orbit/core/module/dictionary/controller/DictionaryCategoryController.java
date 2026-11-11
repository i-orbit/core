package com.inmaytide.orbit.core.module.dictionary.controller;

import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategory;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategoryVO;
import com.inmaytide.orbit.core.module.dictionary.service.DictionaryCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<DictionaryCategoryVO> findAll() {
        return service.findAll();
    }

}
