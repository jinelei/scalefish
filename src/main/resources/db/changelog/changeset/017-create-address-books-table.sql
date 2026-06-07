--liquibase formatted sql

--changeset jinelei:017-create-address-books-table
CREATE TABLE address_books (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
