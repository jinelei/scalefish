package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.BookmarkRequest;
import com.jinelei.scalefish.dto.BookmarkResponse;
import com.jinelei.scalefish.dto.PageResponse;
import com.jinelei.scalefish.entity.Bookmark;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.BookmarkRepository;
import com.jinelei.scalefish.specification.BookmarkSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookmarkService {

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
        Sort sort = Sort.by(Sort.Direction.DESC, "pinned")
            .and(Sort.by(Sort.Direction.DESC, "updatedAt"));
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Bookmark> p = bookmarkRepository.findAll(
            BookmarkSpecification.withFilters(keyword, categoryIds, tagIds, pinned),
            pageable
        );
        return new PageResponse<>(
            p.getContent().stream().map(BookmarkResponse::from).toList(),
            p.getTotalElements(), p.getTotalPages(), p.getNumber()
        );
    }

    public BookmarkResponse getById(Long id) {
        Bookmark b = bookmarkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", id));
        return BookmarkResponse.from(b);
    }

    @Transactional
    public BookmarkResponse create(BookmarkRequest req) {
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
        return BookmarkResponse.from(bookmarkRepository.save(b));
    }

    @Transactional
    public BookmarkResponse update(Long id, BookmarkRequest req) {
        Bookmark b = bookmarkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", id));
        b.setTitle(req.title());
        b.setUrl(req.url());
        b.setDescription(req.description());
        b.setCategory(req.categoryId() != null ? categoryService.getById(req.categoryId()) : null);
        b.setTags(req.tagIds() != null ? tagService.getAllByIds(req.tagIds()) : new java.util.HashSet<>());
        return BookmarkResponse.from(bookmarkRepository.save(b));
    }

    @Transactional
    public void delete(Long id) {
        bookmarkRepository.deleteById(id);
    }

    @Transactional
    public BookmarkResponse togglePin(Long id) {
        Bookmark b = bookmarkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", id));
        b.setPinned(!b.isPinned());
        return BookmarkResponse.from(bookmarkRepository.save(b));
    }

    @Transactional
    public BookmarkResponse recordClick(Long id) {
        Bookmark b = bookmarkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", id));
        b.setClickCount(b.getClickCount() + 1);
        return BookmarkResponse.from(bookmarkRepository.save(b));
    }
}
