package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.AddressBookResponse;
import com.jinelei.scalefish.entity.AddressBook;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.exception.BusinessException;
import com.jinelei.scalefish.exception.ErrorCode;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.AddressBookRepository;
import com.jinelei.scalefish.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookService {

    private static final Logger log = LoggerFactory.getLogger(AddressBookService.class);

    private final AddressBookRepository addressBookRepository;
    private final ContactRepository contactRepository;

    public AddressBookService(AddressBookRepository addressBookRepository,
                               ContactRepository contactRepository) {
        this.addressBookRepository = addressBookRepository;
        this.contactRepository = contactRepository;
    }

    public List<AddressBookResponse> list(User user) {
        log.debug("List address books for user: {}", user.getId());
        return addressBookRepository.findByUserIdOrderByCreatedAtAsc(user.getId()).stream()
            .map(AddressBookResponse::from)
            .toList();
    }

    public AddressBookResponse create(User user, String name, String description) {
        log.info("Create address book: name={}, userId={}", name, user.getId());
        var ab = new AddressBook();
        ab.setName(name);
        ab.setDescription(description);
        ab.setUser(user);
        addressBookRepository.save(ab);
        log.info("Address book created: id={}, name={}", ab.getId(), ab.getName());
        return AddressBookResponse.from(ab);
    }

    public AddressBookResponse update(User user, Long id, String name, String description) {
        log.info("Update address book: id={}, userId={}", id, user.getId());
        var ab = addressBookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AddressBook", id));
        if (!ab.getUser().getId().equals(user.getId())) {
            log.warn("Address book update forbidden: id={}, userId={}", id, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "AddressBook does not belong to the current user");
        }
        if (name != null) ab.setName(name);
        if (description != null) ab.setDescription(description);
        addressBookRepository.save(ab);
        log.info("Address book updated: id={}", ab.getId());
        return AddressBookResponse.from(ab);
    }

    @Transactional
    public void delete(User user, Long id) {
        log.info("Delete address book: id={}, userId={}", id, user.getId());
        var ab = addressBookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AddressBook", id));
        if (!ab.getUser().getId().equals(user.getId())) {
            log.warn("Address book delete forbidden: id={}, userId={}", id, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "AddressBook does not belong to the current user");
        }
        contactRepository.deleteByAddressBookId(id);
        addressBookRepository.delete(ab);
        log.info("Address book deleted: id={}", id);
    }

    public AddressBook getEntity(User user, Long id) {
        log.debug("Get address book entity: id={}, userId={}", id, user.getId());
        var ab = addressBookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AddressBook", id));
        if (!ab.getUser().getId().equals(user.getId())) {
            log.warn("Address book getEntity forbidden: id={}, userId={}", id, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "AddressBook does not belong to the current user");
        }
        return ab;
    }
}
