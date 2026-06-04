package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNullOrderBySortOrder();
    List<Category> findAllByOrderBySortOrder();
    boolean existsByName(String name);
}
