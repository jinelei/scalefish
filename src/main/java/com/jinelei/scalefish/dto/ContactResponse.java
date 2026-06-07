package com.jinelei.scalefish.dto;

import com.jinelei.scalefish.entity.Contact;

import java.time.LocalDateTime;

public record ContactResponse(
    Long id,
    Long addressBookId,
    String name,
    String email,
    String phone,
    String organization,
    String notes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ContactResponse from(Contact c) {
        return new ContactResponse(c.getId(), c.getAddressBook().getId(),
            c.getName(), c.getEmail(), c.getPhone(),
            c.getOrganization(), c.getNotes(),
            c.getCreatedAt(), c.getUpdatedAt());
    }
}
