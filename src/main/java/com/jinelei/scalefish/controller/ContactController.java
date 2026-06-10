package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.ContactRequest;
import com.jinelei.scalefish.dto.ContactResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.service.ContactService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public GenericResult<List<ContactResponse>> list(@AuthenticationPrincipal User user,
                                                      @RequestParam Long addressBookId) {
        log.debug("GET /api/contacts - addressBookId={}, userId={}", addressBookId, user.getId());
        return GenericResult.success(contactService.listByAddressBook(user, addressBookId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<ContactResponse> create(@AuthenticationPrincipal User user,
                                                  @RequestParam Long addressBookId,
                                                  @Valid @RequestBody ContactRequest request) {
        log.info("POST /api/contacts - name={}, addressBookId={}, userId={}", request.name(), addressBookId, user.getId());
        return GenericResult.success(contactService.create(user, addressBookId, request));
    }

    @PutMapping("/{id}")
    public GenericResult<ContactResponse> update(@AuthenticationPrincipal User user,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody ContactRequest request) {
        log.info("PUT /api/contacts/{} - userId={}", id, user.getId());
        return GenericResult.success(contactService.update(user, id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        log.info("DELETE /api/contacts/{} - userId={}", id, user.getId());
        contactService.delete(user, id);
    }
}
