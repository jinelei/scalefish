package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.TagRequest;
import com.jinelei.scalefish.dto.TagResponse;
import com.jinelei.scalefish.dto.TagStatsResponse;
import com.jinelei.scalefish.entity.Bookmark;
import com.jinelei.scalefish.entity.Tag;
import com.jinelei.scalefish.exception.DuplicateResourceException;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.BookmarkRepository;
import com.jinelei.scalefish.repository.TagRepository;
import com.jinelei.scalefish.specification.BookmarkSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    private static final Logger log = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;
    private final BookmarkRepository bookmarkRepository;
    private final EntityManager entityManager;

    public TagService(TagRepository tagRepository, BookmarkRepository bookmarkRepository, EntityManager entityManager) {
        this.tagRepository = tagRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.entityManager = entityManager;
    }

    public List<TagResponse> getAll() {
        log.debug("Get all tags");
        return tagRepository.findAll().stream()
            .map(TagResponse::from)
            .toList();
    }

    public Tag getById(Long id) {
        log.debug("Get tag by id: {}", id);
        return tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag", id));
    }

    public Set<Tag> getAllByIds(Set<Long> ids) {
        log.debug("Get tags by ids: {}", ids);
        return ids.stream().map(this::getById).collect(Collectors.toSet());
    }

    @Transactional
    public Tag create(TagRequest req) {
        log.info("Create tag: name={}", req.name());
        if (tagRepository.existsByName(req.name())) {
            log.warn("Tag already exists: {}", req.name());
            throw new DuplicateResourceException("Tag", req.name());
        }
        Tag saved = tagRepository.save(new Tag(req.name()));
        log.info("Tag created: id={}, name={}", saved.getId(), saved.getName());
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        log.info("Delete tag: id={}", id);
        Tag tag = getById(id);
        tagRepository.delete(tag);
        log.info("Tag deleted: id={}", id);
    }

    @Transactional(readOnly = true)
    public List<TagStatsResponse> getTagStats(List<Long> categoryIds, List<Long> tagIds) {
        log.debug("Get tag stats: categoryIds={}, tagIds={}", categoryIds, tagIds);
        var spec = BookmarkSpecification.withFilters(null, categoryIds, tagIds, null);
        List<Long> bookmarkIds = bookmarkRepository.findAll(spec)
            .stream().map(Bookmark::getId).toList();

        if (bookmarkIds.isEmpty()) {
            return tagRepository.findAll().stream()
                .map(t -> new TagStatsResponse(t.getId(), t.getName(), 0))
                .sorted(Comparator.comparingInt(TagStatsResponse::count).reversed())
                .toList();
        }

        String jpql = """
            SELECT t.id, t.name, COUNT(DISTINCT b.id)
            FROM Bookmark b
            JOIN b.tags t
            WHERE b.id IN :ids
            GROUP BY t.id, t.name
            ORDER BY COUNT(DISTINCT b.id) DESC
            """;

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("ids", bookmarkIds);
        List<Object[]> results = query.getResultList();

        Map<Long, Integer> countMap = results.stream()
            .collect(Collectors.toMap(r -> (Long) r[0], r -> ((Long) r[2]).intValue()));

        return tagRepository.findAll().stream()
            .map(t -> new TagStatsResponse(t.getId(), t.getName(), countMap.getOrDefault(t.getId(), 0)))
            .sorted(Comparator.comparingInt(TagStatsResponse::count).reversed())
            .toList();
    }
}
