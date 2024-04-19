package com.inmaytide.orbit.core.api;

import com.inmaytide.orbit.commons.domain.dto.result.TreeNode;
import com.inmaytide.orbit.core.domain.Dictionary;
import com.inmaytide.orbit.core.domain.DictionaryCategory;
import com.inmaytide.orbit.core.service.DictionaryCategoryService;
import com.inmaytide.orbit.core.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/19
 */
@RestController
@RequestMapping("/api/dictionaries")
@Tag(name = "数据字典")
public class DictionaryResource {

    private final DictionaryCategoryService categoryService;

    private final DictionaryService dictionaryService;

    public DictionaryResource(DictionaryCategoryService categoryService, DictionaryService dictionaryService) {
        this.categoryService = categoryService;
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("categories")
    @Operation(summary = "查询系统当前可用的所有数据字典分类信息列表")
    public List<DictionaryCategory> findCategories() {
        return categoryService.all();
    }

    @GetMapping("tree-by-category")
    @Operation(summary = "查询指定数据字典分类的数据字典树")
    public List<TreeNode<Dictionary>> treeByCategory(@RequestParam String category) {
        return dictionaryService.treeByCategory(category);
    }

}
