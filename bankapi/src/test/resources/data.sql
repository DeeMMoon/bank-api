CREATE TABLE IF NOT EXISTS clients
(
    id                 UUID                  DEFAULT RANDOM_UUID() PRIMARY KEY,
    personal_id        varchar(30)  NOT NULL,
    first_name         varchar(20)  NOT NULL,
    last_name          varchar(20)  NOT NULL,
    age                integer      NOT NULL,
    email              varchar(100) NOT NULL,
    gender             varchar(10)  NOT NULL,
    address            varchar(200) NOT NULL,
    phone              varchar(20),
    created_time       TIMESTAMP    NOT NULL DEFAULT '2023-11-23 00:00:00.000000',
    last_modified_time TIMESTAMP    NOT NULL DEFAULT '2023-11-23 00:00:00.000000'
);

CREATE TABLE IF NOT EXISTS accounts
(
    id            UUID    DEFAULT RANDOM_UUID() PRIMARY KEY,
    number        varchar(20),
    currency_type varchar(3),
    amount        decimal,
    client_id     uuid,
    is_blocked    BOOLEAN DEFAULT FALSE,
    foreign key (client_id) references clients (id)
);

CREATE TABLE IF NOT EXISTS currency_rate
(
    converter_currency VARCHAR(3) NOT NULL,
    converted_currency VARCHAR(3) NOT NULL,
    rate               decimal(10, 6),
    CONSTRAINT currency_rate_pkey PRIMARY KEY (converter_currency, converted_currency)
);

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

INSERT INTO clients
VALUES ('4f9a97c4-8300-11ee-b962-0242ac120010', '4515 193232', 'Ivan', 'Ivanov', 30, 'ivanovIvan@gmail.com', 'MAN',
        'Russia, Moscow, Kutuzovsky, 32, 21', '+7(903)777-7777', '2023-11-23 00:00:00.000000',
        '2023-11-23 00:00:00.000000'),
       ('4f9a97c4-8300-11ee-b962-0242ac120011', '4515 193232', 'Polina', 'Petrova', 40, 'polinaPetrova@mail.ru',
        'WOMAN', 'Russia, Moscow, Kutuzovsky, 31, 21', '+7(900)888-8888', '2023-11-23 00:00:00.000000',
        '2023-11-23 00:00:00.000000'),
       ('4f9a97c4-8300-11ee-b962-0242ac120012', '4515 193232', 'Maks', 'Smirnov', 24, 'maksSmirnov@qwerty.org', 'MAN',
        'Russia, Moscow, Kutuzovsky, 35, 21', '+7(903)123-1234', '2023-11-23 00:00:00.000000',
        '2023-11-23 00:00:00.000000');

INSERT INTO accounts
VALUES ('4f9a97c4-8300-11ee-b962-0242ac120001', '12345678910111213150', 'RUR', 1000,
        '4f9a97c4-8300-11ee-b962-0242ac120010', false),
       ('4f9a97c4-8300-11ee-b962-0242ac120002', '12345678910111213149', 'USD', 100,
        '4f9a97c4-8300-11ee-b962-0242ac120010', false),
       ('4f9a97c4-8300-11ee-b962-0242ac120003', '12345678910111213148', 'RUR', 50,
        '4f9a97c4-8300-11ee-b962-0242ac120011', false),
       ('4f9a97c4-8300-11ee-b962-0242ac120004', '12345678910111213147', 'USD', 0.2,
        '4f9a97c4-8300-11ee-b962-0242ac120012', false),
       ('4f9a97c4-8300-11ee-b962-0242ac120005', '12345678910111213146', 'USD', 404,
        '4f9a97c4-8300-11ee-b962-0242ac120012', true);

INSERT INTO currency_rate
VALUES ('RUR', 'USD', 0.011111),
       ('USD', 'RUR', 90);