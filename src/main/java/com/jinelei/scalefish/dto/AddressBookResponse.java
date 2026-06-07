package com.jinelei.scalefish.dto;

import com.jinelei.scalefish.entity.AddressBook;

import java.time.LocalDateTime;

public record AddressBookResponse(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static AddressBookResponse from(AddressBook b) {
        return new AddressBookResponse(b.getId(), b.getName(),
            b.getDescription(), b.getCreatedAt(), b.getUpdatedAt());
    }
}
