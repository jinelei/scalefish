package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, JpaSpecificationExecutor<Bookmark> {
}
