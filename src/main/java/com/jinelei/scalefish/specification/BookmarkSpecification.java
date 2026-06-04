package com.jinelei.scalefish.specification;

import com.jinelei.scalefish.entity.Bookmark;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class BookmarkSpecification {

    public static Specification<Bookmark> withFilters(String keyword, Long categoryId, Long tagId, Boolean pinned) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("url")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
                ));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (tagId != null) {
                predicates.add(cb.equal(root.join("tags").get("id"), tagId));
            }

            if (pinned != null) {
                predicates.add(cb.equal(root.get("pinned"), pinned));
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
