package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, JpaSpecificationExecutor<Bookmark> {

    @Query("SELECT b.category.id, COUNT(b) FROM Bookmark b WHERE b.category IS NOT NULL GROUP BY b.category.id")
    List<Object[]> countByCategory();
}
