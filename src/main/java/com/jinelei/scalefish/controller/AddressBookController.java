package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.AddressBookResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.service.AddressBookService;
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
@RequestMapping("/api/addressbooks")
public class AddressBookController {

    private static final Logger log = LoggerFactory.getLogger(AddressBookController.class);

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @GetMapping
    public GenericResult<List<AddressBookResponse>> list(@AuthenticationPrincipal User user) {
        log.debug("GET /api/addressbooks - userId={}", user.getId());
        return GenericResult.success(addressBookService.list(user));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<AddressBookResponse> create(@AuthenticationPrincipal User user,
                                                      @RequestParam String name,
                                                      @RequestParam(required = false) String description) {
        log.info("POST /api/addressbooks - name={}, userId={}", name, user.getId());
        return GenericResult.success(addressBookService.create(user, name, description));
    }

    @PutMapping("/{id}")
    public GenericResult<AddressBookResponse> update(@AuthenticationPrincipal User user,
                                                      @PathVariable Long id,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String description) {
        log.info("PUT /api/addressbooks/{} - userId={}", id, user.getId());
        return GenericResult.success(addressBookService.update(user, id, name, description));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        log.info("DELETE /api/addressbooks/{} - userId={}", id, user.getId());
        addressBookService.delete(user, id);
    }
}
