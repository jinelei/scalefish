package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByAddressBookIdOrderByNameAsc(Long addressBookId);
    List<Contact> findByAddressBookUserIdOrderByNameAsc(Long userId);
    void deleteByAddressBookId(Long addressBookId);
}
