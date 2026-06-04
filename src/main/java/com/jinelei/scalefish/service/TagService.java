package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.TagRequest;
import com.jinelei.scalefish.dto.TagResponse;
import com.jinelei.scalefish.entity.Tag;
import com.jinelei.scalefish.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagResponse> getAll() {
        return tagRepository.findAll().stream()
            .map(TagResponse::from)
            .toList();
    }

    public Tag getById(Long id) {
        return tagRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tag not found: " + id));
    }

    public Set<Tag> getAllByIds(Set<Long> ids) {
        return ids.stream().map(this::getById).collect(Collectors.toSet());
    }

    @Transactional
    public Tag create(TagRequest req) {
        if (tagRepository.existsByName(req.name())) {
            throw new RuntimeException("Tag already exists: " + req.name());
        }
        return tagRepository.save(new Tag(req.name()));
    }

    @Transactional
    public void delete(Long id) {
        Tag tag = getById(id);
        tagRepository.delete(tag);
    }
}
