--liquibase formatted sql

--changeset divantsov:1
CREATE TABLE currency_rate
(
    converter_currency VARCHAR(3) NOT NULL,
    converted_currency VARCHAR(3) NOT NULL,
    rate               decimal(10, 6),
    CONSTRAINT currency_rate_pkey PRIMARY KEY (converter_currency, converted_currency)
);

--changeset divantsov:2
CREATE TABLE transactions
(
    id                  UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    number_sender       varchar(20)    NOT NULL,
    number_recipient    varchar(20)    NOT NULL,
    amount              decimal        NOT NULL,
    sender_cur          varchar(3)     NOT NULL,
    recipient_cur       varchar(3)     NOT NULL,
    start_time          TIMESTAMP      NOT NULL,
    current_status_time TIMESTAMP      NOT NULL,
    status              varchar(10)    NOT NULL
);

--changeset divantsov:3
INSERT INTO currency_rate
VALUES ('RUR', 'USD', 0.011111),
       ('USD', 'RUR', 90);