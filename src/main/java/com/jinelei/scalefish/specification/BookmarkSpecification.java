package com.jinelei.scalefish.specification;

import com.jinelei.scalefish.entity.Bookmark;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class BookmarkSpecification {

    public static Specification<Bookmark> withFilters(String keyword, List<Long> categoryIds, List<Long> tagIds, Boolean pinned) {
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

            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categoryIds));
            }

            if (tagIds != null && !tagIds.isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                var subRoot = subquery.from(Bookmark.class);
                var tagJoin = subRoot.join("tags");
                subquery.select(subRoot.get("id"));
                subquery.where(tagJoin.get("id").in(tagIds));
                subquery.groupBy(subRoot.get("id"));
                subquery.having(cb.equal(cb.count(subRoot.get("id")), (long) tagIds.size()));
                predicates.add(root.get("id").in(subquery));
            }

            if (pinned != null) {
                predicates.add(cb.equal(root.get("pinned"), pinned));
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
