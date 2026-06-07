--liquibase formatted sql

--changeset jinelei:018-create-contacts-table
CREATE TABLE contacts (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(50),
    organization VARCHAR(255),
    notes TEXT,
    vcard_data TEXT,
    address_book_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (address_book_id) REFERENCES address_books(id) ON DELETE CASCADE
);

CREATE INDEX idx_contacts_address_book ON contacts(address_book_id);
