package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.ExternalLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExternalLinkRepository extends JpaRepository<ExternalLink, Long> {
    List<ExternalLink> findAllByOrderBySortOrder();
}
