package com.jinelei.scalefish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
public class Contact extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 255)
    private String organization;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "vcard_data", columnDefinition = "TEXT")
    private String vcardData;

    @ManyToOne
    @JoinColumn(name = "address_book_id", nullable = false)
    private AddressBook addressBook;
}
