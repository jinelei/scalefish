package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.CalendarResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {

    private static final Logger log = LoggerFactory.getLogger(CalendarController.class);

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public GenericResult<List<CalendarResponse>> list(@AuthenticationPrincipal User user) {
        log.debug("GET /api/calendars - userId={}", user.getId());
        return GenericResult.success(calendarService.list(user));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<CalendarResponse> create(@AuthenticationPrincipal User user,
                                                   @RequestParam String name,
                                                   @RequestParam(required = false) String description,
                                                   @RequestParam(required = false) String displayColor) {
        log.info("POST /api/calendars - name={}, userId={}", name, user.getId());
        return GenericResult.success(calendarService.create(user, name, description, displayColor));
    }

    @PutMapping("/{id}")
    public GenericResult<CalendarResponse> update(@AuthenticationPrincipal User user,
                                                   @PathVariable Long id,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String description,
                                                   @RequestParam(required = false) String displayColor) {
        log.info("PUT /api/calendars/{} - userId={}", id, user.getId());
        return GenericResult.success(calendarService.update(user, id, name, description, displayColor));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        log.info("DELETE /api/calendars/{} - userId={}", id, user.getId());
        calendarService.delete(user, id);
    }
}
