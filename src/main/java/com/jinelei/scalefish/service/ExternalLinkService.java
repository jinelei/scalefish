package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.ExternalLinkRequest;
import com.jinelei.scalefish.dto.ExternalLinkResponse;
import com.jinelei.scalefish.entity.ExternalLink;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.ExternalLinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExternalLinkService {

    private static final Logger log = LoggerFactory.getLogger(ExternalLinkService.class);

    private final ExternalLinkRepository externalLinkRepository;

    public ExternalLinkService(ExternalLinkRepository externalLinkRepository) {
        this.externalLinkRepository = externalLinkRepository;
    }

    public List<ExternalLinkResponse> getAll() {
        log.debug("Get all external links");
        return externalLinkRepository.findAllByOrderBySortOrder().stream()
            .map(ExternalLinkResponse::from)
            .toList();
    }

    public ExternalLink getById(Long id) {
        return externalLinkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ExternalLink", id));
    }

    @Transactional
    public ExternalLinkResponse create(ExternalLinkRequest req) {
        log.info("Create external link: name={}, url={}", req.name(), req.url());
        ExternalLink link = new ExternalLink(req.name(), req.url(), req.icon(), req.sortOrder());
        ExternalLink saved = externalLinkRepository.save(link);
        log.info("External link created: id={}, name={}", saved.getId(), saved.getName());
        return ExternalLinkResponse.from(saved);
    }

    @Transactional
    public ExternalLinkResponse update(Long id, ExternalLinkRequest req) {
        log.info("Update external link: id={}, name={}", id, req.name());
        ExternalLink link = getById(id);
        link.setName(req.name());
        link.setUrl(req.url());
        link.setIcon(req.icon());
        link.setSortOrder(req.sortOrder());
        ExternalLink saved = externalLinkRepository.save(link);
        log.info("External link updated: id={}, name={}", saved.getId(), saved.getName());
        return ExternalLinkResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Delete external link: id={}", id);
        externalLinkRepository.deleteById(id);
        log.info("External link deleted: id={}", id);
    }
}
