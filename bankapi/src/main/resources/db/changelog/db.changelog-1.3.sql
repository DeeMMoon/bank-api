--liquibase formatted sql

--changeset divantsov:1
CREATE TABLE transactions
(
    id                  UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    number_sender       varchar(20) NOT NULL,
    number_recipient    varchar(20) NOT NULL,
    amount              decimal     NOT NULL,
    sender_cur          varchar(3)  NOT NULL,
    recipient_cur       varchar(3)  NOT NULL,
    start_time          TIMESTAMP   NOT NULL,
    current_status_time TIMESTAMP   NOT NULL,
    status              varchar(10) NOT NULL
);