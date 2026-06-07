package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.CalendarResponse;
import com.jinelei.scalefish.entity.CalendarEntity;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.CalendarEventRepository;
import com.jinelei.scalefish.repository.CalendarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarEventRepository calendarEventRepository;

    public CalendarService(CalendarRepository calendarRepository,
                           CalendarEventRepository calendarEventRepository) {
        this.calendarRepository = calendarRepository;
        this.calendarEventRepository = calendarEventRepository;
    }

    public List<CalendarResponse> list(User user) {
        return calendarRepository.findByUserIdOrderByCreatedAtAsc(user.getId()).stream()
            .map(CalendarResponse::from)
            .toList();
    }

    public CalendarResponse create(User user, String name, String description, String displayColor) {
        var cal = new CalendarEntity();
        cal.setName(name);
        cal.setDescription(description);
        cal.setDisplayColor(displayColor);
        cal.setUser(user);
        calendarRepository.save(cal);
        return CalendarResponse.from(cal);
    }

    public CalendarResponse update(User user, Long id, String name, String description, String displayColor) {
        var cal = calendarRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calendar", id));
        if (!cal.getUser().getId().equals(user.getId())) {
            throw new com.jinelei.scalefish.exception.BusinessException(
                com.jinelei.scalefish.exception.ErrorCode.FORBIDDEN, "Calendar does not belong to the current user");
        }
        if (name != null) cal.setName(name);
        if (description != null) cal.setDescription(description);
        if (displayColor != null) cal.setDisplayColor(displayColor);
        calendarRepository.save(cal);
        return CalendarResponse.from(cal);
    }

    @Transactional
    public void delete(User user, Long id) {
        var cal = calendarRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calendar", id));
        if (!cal.getUser().getId().equals(user.getId())) {
            throw new com.jinelei.scalefish.exception.BusinessException(
                com.jinelei.scalefish.exception.ErrorCode.FORBIDDEN, "Calendar does not belong to the current user");
        }
        calendarEventRepository.deleteByCalendarId(id);
        calendarRepository.delete(cal);
    }

    public CalendarEntity getEntity(User user, Long id) {
        var cal = calendarRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calendar", id));
        if (!cal.getUser().getId().equals(user.getId())) {
            throw new com.jinelei.scalefish.exception.BusinessException(
                com.jinelei.scalefish.exception.ErrorCode.FORBIDDEN, "Calendar does not belong to the current user");
        }
        return cal;
    }
}
