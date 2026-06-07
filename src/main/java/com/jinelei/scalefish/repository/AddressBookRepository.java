package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
    List<AddressBook> findByUserIdOrderByCreatedAtAsc(Long userId);
}
