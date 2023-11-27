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
INSERT INTO currency_rate
VALUES ('RUR', 'USD', 0.011111),
       ('USD', 'RUR', 90);