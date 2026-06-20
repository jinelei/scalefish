package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNullOrderBySortOrder();
    List<Category> findAllByOrderBySortOrder();
    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE Category c SET c.parent = NULL WHERE c.parent.id = :parentId")
    void clearParentFromChildren(@Param("parentId") Long parentId);
}
