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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final BookmarkRepository bookmarkRepository;
    private final EntityManager entityManager;

    public TagService(TagRepository tagRepository, BookmarkRepository bookmarkRepository, EntityManager entityManager) {
        this.tagRepository = tagRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.entityManager = entityManager;
    }

    public List<TagResponse> getAll() {
        return tagRepository.findAll().stream()
            .map(TagResponse::from)
            .toList();
    }

    public Tag getById(Long id) {
        return tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag", id));
    }

    public Set<Tag> getAllByIds(Set<Long> ids) {
        return ids.stream().map(this::getById).collect(Collectors.toSet());
    }

    @Transactional
    public Tag create(TagRequest req) {
        if (tagRepository.existsByName(req.name())) {
            throw new DuplicateResourceException("Tag", req.name());
        }
        return tagRepository.save(new Tag(req.name()));
    }

    @Transactional
    public void delete(Long id) {
        Tag tag = getById(id);
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    public List<TagStatsResponse> getTagStats(List<Long> categoryIds, List<Long> tagIds) {
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
