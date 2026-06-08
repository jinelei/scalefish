package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    List<CalendarEvent> findByCalendarIdOrderByStartTimeAsc(Long calendarId);
    List<CalendarEvent> findByCalendarIdAndStartTimeBetweenOrderByStartTimeAsc(
            Long calendarId, LocalDateTime start, LocalDateTime end);
    List<CalendarEvent> findByCalendarUserIdOrderByStartTimeAsc(Long userId);
    List<CalendarEvent> findByCalendarUserIdAndStartTimeBetweenOrderByStartTimeAsc(
            Long userId, LocalDateTime start, LocalDateTime end);
    java.util.Optional<CalendarEvent> findByUidAndCalendarId(String uid, Long calendarId);
    void deleteByCalendarId(Long calendarId);
}
