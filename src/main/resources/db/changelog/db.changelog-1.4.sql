--liquibase formatted sql

--changeset divantsov:1
ALTER TABLE accounts
    ADD (is_blocked BOOLEAN DEFAULT FALSE);

--changeset divantsov:2
INSERT INTO accounts
VALUES ('4f9a97c4-8300-11ee-b962-0242ac120005', '12345678910111213146', 'USD', 404,
        '4f9a97c4-8300-11ee-b962-0242ac120012', true);