package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, JpaSpecificationExecutor<Bookmark> {

    @Query("SELECT b.category.id, COUNT(b) FROM Bookmark b WHERE b.category IS NOT NULL GROUP BY b.category.id")
    List<Object[]> countByCategory();

    @Modifying
    @Query("UPDATE Bookmark b SET b.category = NULL WHERE b.category.id = :categoryId")
    void clearCategoryFromBookmarks(@Param("categoryId") Long categoryId);

    @Modifying
    @Query(value = "DELETE FROM bookmark_tags WHERE tag_id = :tagId", nativeQuery = true)
    void removeTagFromAllBookmarks(@Param("tagId") Long tagId);
}
