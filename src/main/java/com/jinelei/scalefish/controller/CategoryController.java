package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.CategoryRequest;
import com.jinelei.scalefish.dto.CategoryResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.entity.Category;
import com.jinelei.scalefish.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "分类管理", description = "分类的增删改查，支持树形结构")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "获取分类树", description = "返回树形结构的全部分类")
    @GetMapping
    public GenericResult<List<CategoryResponse>> getTree() {
        return GenericResult.success(categoryService.getTree());
    }

    @Operation(summary = "获取分类书签统计", description = "返回每个分类及其子分类的书签数量")
    @GetMapping("/stats")
    public GenericResult<List<com.jinelei.scalefish.dto.CategoryStatsResponse>> getStats() {
        return GenericResult.success(categoryService.getStats());
    }

    @Operation(summary = "创建分类")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<Category> create(@Valid @RequestBody CategoryRequest req) {
        return GenericResult.created(categoryService.create(req));
    }

    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    public GenericResult<Category> update(@Parameter(description = "分类 ID") @PathVariable Long id,
                                           @Valid @RequestBody CategoryRequest req) {
        return GenericResult.success(categoryService.update(id, req));
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResult<Void> delete(@Parameter(description = "分类 ID") @PathVariable Long id) {
        categoryService.delete(id);
        return GenericResult.noContent();
    }
}
