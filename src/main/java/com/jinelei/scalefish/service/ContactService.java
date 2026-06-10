package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.ContactRequest;
import com.jinelei.scalefish.dto.ContactResponse;
import com.jinelei.scalefish.entity.Contact;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.exception.BusinessException;
import com.jinelei.scalefish.exception.ErrorCode;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.AddressBookRepository;
import com.jinelei.scalefish.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final AddressBookRepository addressBookRepository;
    private final AddressBookService addressBookService;

    public ContactService(ContactRepository contactRepository,
                           AddressBookRepository addressBookRepository,
                           AddressBookService addressBookService) {
        this.contactRepository = contactRepository;
        this.addressBookRepository = addressBookRepository;
        this.addressBookService = addressBookService;
    }

    public List<ContactResponse> listByAddressBook(User user, Long addressBookId) {
        log.debug("List contacts for addressBook: {}, userId={}", addressBookId, user.getId());
        addressBookService.getEntity(user, addressBookId);
        return contactRepository.findByAddressBookIdOrderByNameAsc(addressBookId).stream()
            .map(ContactResponse::from)
            .toList();
    }

    public ContactResponse create(User user, Long addressBookId, ContactRequest request) {
        log.info("Create contact: name={}, addressBookId={}, userId={}", request.name(), addressBookId, user.getId());
        var ab = addressBookService.getEntity(user, addressBookId);
        var contact = new Contact();
        contact.setName(request.name());
        contact.setEmail(request.email());
        contact.setPhone(request.phone());
        contact.setOrganization(request.organization());
        contact.setNotes(request.notes());
        contact.setAddressBook(ab);
        contact.setVcardData(buildVcard(contact));
        contactRepository.save(contact);
        log.info("Contact created: id={}, name={}", contact.getId(), contact.getName());
        return ContactResponse.from(contact);
    }

    public ContactResponse update(User user, Long contactId, ContactRequest request) {
        log.info("Update contact: id={}, userId={}", contactId, user.getId());
        var contact = contactRepository.findById(contactId)
            .orElseThrow(() -> new ResourceNotFoundException("Contact", contactId));
        if (!contact.getAddressBook().getUser().getId().equals(user.getId())) {
            log.warn("Contact update forbidden: id={}, userId={}", contactId, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "Contact does not belong to the current user");
        }
        contact.setName(request.name());
        contact.setEmail(request.email());
        contact.setPhone(request.phone());
        contact.setOrganization(request.organization());
        contact.setNotes(request.notes());
        contact.setVcardData(buildVcard(contact));
        contactRepository.save(contact);
        log.info("Contact updated: id={}", contact.getId());
        return ContactResponse.from(contact);
    }

    public void delete(User user, Long contactId) {
        log.info("Delete contact: id={}, userId={}", contactId, user.getId());
        var contact = contactRepository.findById(contactId)
            .orElseThrow(() -> new ResourceNotFoundException("Contact", contactId));
        if (!contact.getAddressBook().getUser().getId().equals(user.getId())) {
            log.warn("Contact delete forbidden: id={}, userId={}", contactId, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "Contact does not belong to the current user");
        }
        contactRepository.delete(contact);
        log.info("Contact deleted: id={}", contactId);
    }

    private String buildVcard(Contact contact) {
        var sb = new StringBuilder();
        sb.append("BEGIN:VCARD\r\n");
        sb.append("VERSION:3.0\r\n");
        sb.append("FN:").append(contact.getName()).append("\r\n");
        sb.append("N:").append(contact.getName()).append(";;;\r\n");
        if (contact.getEmail() != null) {
            sb.append("EMAIL;TYPE=INTERNET:").append(contact.getEmail()).append("\r\n");
        }
        if (contact.getPhone() != null) {
            sb.append("TEL:").append(contact.getPhone()).append("\r\n");
        }
        if (contact.getOrganization() != null) {
            sb.append("ORG:").append(contact.getOrganization()).append("\r\n");
        }
        if (contact.getNotes() != null) {
            sb.append("NOTE:").append(contact.getNotes().replace("\n", "\\n")).append("\r\n");
        }
        sb.append("END:VCARD\r\n");
        return sb.toString();
    }

    public Contact getEntity(User user, Long contactId) {
        log.debug("Get contact entity: id={}, userId={}", contactId, user.getId());
        var contact = contactRepository.findById(contactId)
            .orElseThrow(() -> new ResourceNotFoundException("Contact", contactId));
        if (!contact.getAddressBook().getUser().getId().equals(user.getId())) {
            log.warn("Contact getEntity forbidden: id={}, userId={}", contactId, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "Contact does not belong to the current user");
        }
        return contact;
    }
}
