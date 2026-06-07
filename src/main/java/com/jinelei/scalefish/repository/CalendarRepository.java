package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {
    List<CalendarEntity> findByUserIdOrderByCreatedAtAsc(Long userId);
}
