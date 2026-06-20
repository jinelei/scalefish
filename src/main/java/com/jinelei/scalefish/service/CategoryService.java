package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.CategoryRequest;
import com.jinelei.scalefish.dto.CategoryResponse;
import com.jinelei.scalefish.dto.CategoryStatsResponse;
import com.jinelei.scalefish.entity.Category;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.BookmarkRepository;
import com.jinelei.scalefish.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final BookmarkRepository bookmarkRepository;

    public CategoryService(CategoryRepository categoryRepository, BookmarkRepository bookmarkRepository) {
        this.categoryRepository = categoryRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    public List<CategoryResponse> getTree() {
        log.debug("Get category tree");
        List<Category> all = categoryRepository.findAllByOrderBySortOrder();
        log.debug("Total categories: {}", all.size());
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
        log.debug("Get category by id: {}", id);
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    @Transactional
    public Category create(CategoryRequest req) {
        log.info("Create category: name={}, parentId={}", req.name(), req.parentId());
        Category cat = new Category(req.name());
        cat.setSortOrder(req.sortOrder() != null ? req.sortOrder() : 0);
        if (req.parentId() != null) {
            cat.setParent(getById(req.parentId()));
        }
        Category saved = categoryRepository.save(cat);
        log.info("Category created: id={}, name={}", saved.getId(), saved.getName());
        return saved;
    }

    @Transactional
    public Category update(Long id, CategoryRequest req) {
        log.info("Update category: id={}, name={}", id, req.name());
        Category cat = getById(id);
        cat.setName(req.name());
        cat.setSortOrder(req.sortOrder() != null ? req.sortOrder() : cat.getSortOrder());
        if (req.parentId() != null) {
            cat.setParent(getById(req.parentId()));
        } else {
            cat.setParent(null);
        }
        Category saved = categoryRepository.save(cat);
        log.info("Category updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        log.info("Delete category: id={}", id);
        categoryRepository.clearParentFromChildren(id);
        bookmarkRepository.clearCategoryFromBookmarks(id);
        categoryRepository.deleteById(id);
        log.info("Category deleted: id={}", id);
    }

    public List<CategoryStatsResponse> getStats() {
        log.debug("Get category stats");
        List<Object[]> raw = bookmarkRepository.countByCategory();
        Map<Long, Integer> direct = new HashMap<>();
        for (Object[] row : raw) {
            Long catId = (Long) row[0];
            Long count = (Long) row[1];
            direct.put(catId, count.intValue());
        }

        List<Category> all = categoryRepository.findAllByOrderBySortOrder();
        Map<Long, List<Long>> parentToChildren = new HashMap<>();
        Map<Long, String> idToName = new HashMap<>();
        for (Category c : all) {
            idToName.put(c.getId(), c.getName());
            Long parentId = c.getParent() != null ? c.getParent().getId() : 0L;
            parentToChildren.computeIfAbsent(parentId, k -> new ArrayList<>()).add(c.getId());
        }

        Map<Long, Integer> aggregated = new HashMap<>();
        for (Category c : all) {
            aggregateCount(c.getId(), parentToChildren, direct, aggregated, new HashMap<>());
        }

        return all.stream()
            .map(c -> new CategoryStatsResponse(c.getId(), c.getName(), aggregated.getOrDefault(c.getId(), 0)))
            .toList();
    }

    private int aggregateCount(Long id, Map<Long, List<Long>> parentToChildren,
                                Map<Long, Integer> direct, Map<Long, Integer> aggregated,
                                Map<Long, Integer> memo) {
        if (memo.containsKey(id)) return memo.get(id);
        int total = direct.getOrDefault(id, 0);
        for (Long childId : parentToChildren.getOrDefault(id, List.of())) {
            total += aggregateCount(childId, parentToChildren, direct, aggregated, memo);
        }
        memo.put(id, total);
        aggregated.put(id, total);
        return total;
    }
}
