--liquibase formatted sql

--changeset divantsov:1
ALTER TABLE clients
    ADD (created_time TIMESTAMP NOT NULL DEFAULT '2023-11-21 23:29:59.138451',
         last_modified_time TIMESTAMP NOT NULL DEFAULT '2023-11-21 23:29:59.138451');

--changeset divantsov:2
CREATE TABLE clients_history
(
    id            BIGINT auto_increment PRIMARY KEY,
    client_id     UUID         NOT NULL,
    personal_id   varchar(30)  NOT NULL,
    first_name    varchar(20)  NOT NULL,
    last_name     varchar(20)  NOT NULL,
    age           integer      NOT NULL,
    email         varchar(100) NOT NULL,
    gender        varchar(10)  NOT NULL,
    address       varchar(200) NOT NULL,
    phone         varchar(20),
    modified_time TIMESTAMP    NOT NULL
);