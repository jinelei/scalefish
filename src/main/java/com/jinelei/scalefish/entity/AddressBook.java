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
@Table(name = "address_books")
@Getter
@Setter
@NoArgsConstructor
public class AddressBook extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
