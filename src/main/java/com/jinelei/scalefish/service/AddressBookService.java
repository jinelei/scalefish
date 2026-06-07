package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.AddressBookResponse;
import com.jinelei.scalefish.entity.AddressBook;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.exception.BusinessException;
import com.jinelei.scalefish.exception.ErrorCode;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.AddressBookRepository;
import com.jinelei.scalefish.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;
    private final ContactRepository contactRepository;

    public AddressBookService(AddressBookRepository addressBookRepository,
                               ContactRepository contactRepository) {
        this.addressBookRepository = addressBookRepository;
        this.contactRepository = contactRepository;
    }

    public List<AddressBookResponse> list(User user) {
        return addressBookRepository.findByUserIdOrderByCreatedAtAsc(user.getId()).stream()
            .map(AddressBookResponse::from)
            .toList();
    }

    public AddressBookResponse create(User user, String name, String description) {
        var ab = new AddressBook();
        ab.setName(name);
        ab.setDescription(description);
        ab.setUser(user);
        addressBookRepository.save(ab);
        return AddressBookResponse.from(ab);
    }

    public AddressBookResponse update(User user, Long id, String name, String description) {
        var ab = addressBookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AddressBook", id));
        if (!ab.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "AddressBook does not belong to the current user");
        }
        if (name != null) ab.setName(name);
        if (description != null) ab.setDescription(description);
        addressBookRepository.save(ab);
        return AddressBookResponse.from(ab);
    }

    @Transactional
    public void delete(User user, Long id) {
        var ab = addressBookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AddressBook", id));
        if (!ab.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "AddressBook does not belong to the current user");
        }
        contactRepository.deleteByAddressBookId(id);
        addressBookRepository.delete(ab);
    }

    public AddressBook getEntity(User user, Long id) {
        var ab = addressBookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AddressBook", id));
        if (!ab.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "AddressBook does not belong to the current user");
        }
        return ab;
    }
}
