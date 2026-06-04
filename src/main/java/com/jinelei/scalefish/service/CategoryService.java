package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.CategoryRequest;
import com.jinelei.scalefish.dto.CategoryResponse;
import com.jinelei.scalefish.entity.Category;
import com.jinelei.scalefish.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getTree() {
        List<Category> all = categoryRepository.findAllByOrderBySortOrder();
        Map<Long, List<CategoryResponse>> grouped = all.stream()
            .collect(Collectors.groupingBy(
                c -> c.getParent() == null ? 0L : c.getParent().getId(),
                Collectors.mapping(this::toResponseWithoutChildren, Collectors.toList())
            ));
        List<CategoryResponse> roots = grouped.getOrDefault(0L, new ArrayList<>());
        for (CategoryResponse node : flatten(all)) {
            Long id = node.id();
            if (grouped.containsKey(id)) {
                int idx = roots.indexOf(node);
                if (idx >= 0) {
                    CategoryResponse enriched = new CategoryResponse(
                        node.id(), node.name(), node.sortOrder(),
                        grouped.get(id)
                    );
                    roots.set(idx, enriched);
                }
            }
        }
        return roots;
    }

    private List<CategoryResponse> flatten(List<Category> all) {
        return all.stream().map(this::toResponseWithoutChildren).toList();
    }

    private CategoryResponse toResponseWithoutChildren(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getSortOrder(), new ArrayList<>());
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found: " + id));
    }

    @Transactional
    public Category create(CategoryRequest req) {
        Category cat = new Category(req.name());
        cat.setSortOrder(req.sortOrder() != null ? req.sortOrder() : 0);
        if (req.parentId() != null) {
            cat.setParent(getById(req.parentId()));
        }
        return categoryRepository.save(cat);
    }

    @Transactional
    public Category update(Long id, CategoryRequest req) {
        Category cat = getById(id);
        cat.setName(req.name());
        cat.setSortOrder(req.sortOrder() != null ? req.sortOrder() : cat.getSortOrder());
        if (req.parentId() != null) {
            cat.setParent(getById(req.parentId()));
        } else {
            cat.setParent(null);
        }
        return categoryRepository.save(cat);
    }

    @Transactional
    public void delete(Long id) {
        Category cat = getById(id);
        categoryRepository.delete(cat);
    }
}
