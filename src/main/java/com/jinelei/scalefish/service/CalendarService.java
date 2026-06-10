package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.CalendarResponse;
import com.jinelei.scalefish.entity.CalendarEntity;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.CalendarEventRepository;
import com.jinelei.scalefish.repository.CalendarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CalendarService {

    private static final Logger log = LoggerFactory.getLogger(CalendarService.class);

    private final CalendarRepository calendarRepository;
    private final CalendarEventRepository calendarEventRepository;

    public CalendarService(CalendarRepository calendarRepository,
                           CalendarEventRepository calendarEventRepository) {
        this.calendarRepository = calendarRepository;
        this.calendarEventRepository = calendarEventRepository;
    }

    public List<CalendarResponse> list(User user) {
        log.debug("List calendars for user: {}", user.getId());
        return calendarRepository.findByUserIdOrderByCreatedAtAsc(user.getId()).stream()
            .map(CalendarResponse::from)
            .toList();
    }

    public CalendarResponse create(User user, String name, String description, String displayColor) {
        log.info("Create calendar: name={}, userId={}", name, user.getId());
        var cal = new CalendarEntity();
        cal.setName(name);
        cal.setDescription(description);
        cal.setDisplayColor(displayColor);
        cal.setUser(user);
        calendarRepository.save(cal);
        log.info("Calendar created: id={}, name={}", cal.getId(), cal.getName());
        return CalendarResponse.from(cal);
    }

    public CalendarResponse update(User user, Long id, String name, String description, String displayColor) {
        log.info("Update calendar: id={}, userId={}", id, user.getId());
        var cal = calendarRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calendar", id));
        if (!cal.getUser().getId().equals(user.getId())) {
            log.warn("Calendar update forbidden: id={}, userId={}", id, user.getId());
            throw new com.jinelei.scalefish.exception.BusinessException(
                com.jinelei.scalefish.exception.ErrorCode.FORBIDDEN, "Calendar does not belong to the current user");
        }
        if (name != null) cal.setName(name);
        if (description != null) cal.setDescription(description);
        if (displayColor != null) cal.setDisplayColor(displayColor);
        calendarRepository.save(cal);
        log.info("Calendar updated: id={}", cal.getId());
        return CalendarResponse.from(cal);
    }

    @Transactional
    public void delete(User user, Long id) {
        log.info("Delete calendar: id={}, userId={}", id, user.getId());
        var cal = calendarRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calendar", id));
        if (!cal.getUser().getId().equals(user.getId())) {
            log.warn("Calendar delete forbidden: id={}, userId={}", id, user.getId());
            throw new com.jinelei.scalefish.exception.BusinessException(
                com.jinelei.scalefish.exception.ErrorCode.FORBIDDEN, "Calendar does not belong to the current user");
        }
        calendarEventRepository.deleteByCalendarId(id);
        calendarRepository.delete(cal);
        log.info("Calendar deleted: id={}", id);
    }

    public CalendarEntity getEntity(User user, Long id) {
        log.debug("Get calendar entity: id={}, userId={}", id, user.getId());
        var cal = calendarRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calendar", id));
        if (!cal.getUser().getId().equals(user.getId())) {
            log.warn("Calendar getEntity forbidden: id={}, userId={}", id, user.getId());
            throw new com.jinelei.scalefish.exception.BusinessException(
                com.jinelei.scalefish.exception.ErrorCode.FORBIDDEN, "Calendar does not belong to the current user");
        }
        return cal;
    }
}
