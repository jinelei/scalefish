package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.BookmarkRequest;
import com.jinelei.scalefish.dto.BookmarkResponse;
import com.jinelei.scalefish.dto.PageResponse;
import com.jinelei.scalefish.entity.Bookmark;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.BookmarkRepository;
import com.jinelei.scalefish.specification.BookmarkSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookmarkService {

    private static final Logger log = LoggerFactory.getLogger(BookmarkService.class);

    private final BookmarkRepository bookmarkRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    public BookmarkService(BookmarkRepository bookmarkRepository,
                           CategoryService categoryService,
                           TagService tagService) {
        this.bookmarkRepository = bookmarkRepository;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    public PageResponse<BookmarkResponse> search(String keyword, List<Long> categoryIds, List<Long> tagIds,
                                                  Boolean pinned, int page, int size) {
        List<Long> expandedCategoryIds = categoryIds != null && !categoryIds.isEmpty()
            ? new ArrayList<>(categoryService.getDescendantIds(categoryIds))
            : categoryIds;
        log.debug("Search bookmarks: keyword={}, categoryIds={}, tagIds={}, pinned={}, page={}, size={}",
            keyword, expandedCategoryIds, tagIds, pinned, page, size);
        Sort sort = Sort.by(Sort.Direction.DESC, "pinned")
            .and(Sort.by(Sort.Direction.DESC, "updatedAt"));
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Bookmark> p = bookmarkRepository.findAll(
            BookmarkSpecification.withFilters(keyword, expandedCategoryIds, tagIds, pinned),
            pageable
        );
        log.debug("Search results: total={}, pages={}", p.getTotalElements(), p.getTotalPages());
        return new PageResponse<>(
            p.getContent().stream().map(BookmarkResponse::from).toList(),
            p.getTotalElements(), p.getTotalPages(), p.getNumber()
        );
    }

    public BookmarkResponse getById(Long id) {
        log.debug("Get bookmark by id: {}", id);
        Bookmark b = bookmarkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", id));
        return BookmarkResponse.from(b);
    }

    @Transactional
    public BookmarkResponse create(BookmarkRequest req) {
        log.info("Create bookmark: title={}, url={}", req.title(), req.url());
        Bookmark b = new Bookmark();
        b.setTitle(req.title());
        b.setUrl(req.url());
        b.setDescription(req.description());
        if (req.categoryId() != null) {
            b.setCategory(categoryService.getById(req.categoryId()));
        }
        if (req.tagIds() != null && !req.tagIds().isEmpty()) {
            b.setTags(tagService.getAllByIds(req.tagIds()));
        }
        if (req.createdAt() != null) {
            b.setCreatedAt(req.createdAt());
        }
        if (req.updatedAt() != null) {
            b.setUpdatedAt(req.updatedAt());
        }
        if (req.createdAt() != null && req.updatedAt() == null) {
            b.setUpdatedAt(req.createdAt());
        }
        Bookmark saved = bookmarkRepository.save(b);
        log.info("Bookmark created: id={}, title={}", saved.getId(), saved.getTitle());
        return BookmarkResponse.from(saved);
    }

    @Transactional
    public BookmarkResponse update(Long id, BookmarkRequest req) {
        log.info("Update bookmark: id={}, title={}", id, req.title());
        Bookmark b = bookmarkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", id));
        b.setTitle(req.title());
        b.setUrl(req.url());
        b.setDescription(req.description());
        b.setCategory(req.categoryId() != null ? categoryService.getById(req.categoryId()) : null);
        b.setTags(req.tagIds() != null ? tagService.getAllByIds(req.tagIds()) : new java.util.HashSet<>());
        Bookmark saved = bookmarkRepository.save(b);
        log.info("Bookmark updated: id={}", saved.getId());
        return BookmarkResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Delete bookmark: id={}", id);
        bookmarkRepository.deleteById(id);
    }

    @Transactional
    public void batchUpdate(Set<Long> ids, Long categoryId, Set<Long> addTagIds, Set<Long> removeTagIds) {
        if (ids == null || ids.isEmpty()) return;
        log.info("Batch update bookmarks: ids={}, categoryId={}, addTags={}, removeTags={}",
            ids, categoryId, addTagIds, removeTagIds);
        List<Bookmark> bookmarks = bookmarkRepository.findAllById(ids);
        for (Bookmark b : bookmarks) {
            if (categoryId != null) {
                b.setCategory(categoryService.getById(categoryId));
            }
            if (addTagIds != null && !addTagIds.isEmpty()) {
                b.getTags().addAll(tagService.getAllByIds(addTagIds));
            }
            if (removeTagIds != null && !removeTagIds.isEmpty()) {
                b.getTags().removeAll(tagService.getAllByIds(removeTagIds));
            }
        }
        bookmarkRepository.saveAll(bookmarks);
        log.info("Batch update completed: {} bookmarks updated", bookmarks.size());
    }

    @Transactional
    public BookmarkResponse togglePin(Long id) {
        log.debug("Toggle pin bookmark: id={}", id);
        Bookmark b = bookmarkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", id));
        b.setPinned(!b.isPinned());
        Bookmark saved = bookmarkRepository.save(b);
        log.info("Bookmark pin toggled: id={}, pinned={}", saved.getId(), saved.isPinned());
        return BookmarkResponse.from(saved);
    }

    @Transactional
    public BookmarkResponse recordClick(Long id) {
        log.debug("Record click bookmark: id={}", id);
        Bookmark b = bookmarkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", id));
        b.setClickCount(b.getClickCount() + 1);
        Bookmark saved = bookmarkRepository.save(b);
        log.debug("Bookmark click recorded: id={}, count={}", saved.getId(), saved.getClickCount());
        return BookmarkResponse.from(saved);
    }
}
