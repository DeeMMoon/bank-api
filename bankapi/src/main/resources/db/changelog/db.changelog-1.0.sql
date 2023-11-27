--liquibase formatted sql

--changeset divantsov:1
CREATE TABLE clients
(
    id          UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    personal_id varchar(30)  NOT NULL,
    first_name  varchar(20)  NOT NULL,
    last_name   varchar(20)  NOT NULL,
    age         integer      NOT NULL,
    email       varchar(100) NOT NULL,
    gender      varchar(10)  NOT NULL,
    address     varchar(200) NOT NULL,
    phone       varchar(20)
);
--changeset divantsov:2
CREATE TABLE accounts
(
    id            UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    number        varchar(20),
    currency_type varchar(3),
    amount        decimal,
    client_id     uuid,
    foreign key (client_id) references clients (id)
);

--changeset divantsov:3
INSERT INTO clients
VALUES ('4f9a97c4-8300-11ee-b962-0242ac120010', '4515 193232', 'Ivan', 'Ivanov', 30, 'ivanovIvan@gmail.com', 'MAN',
        'Russia, Moscow, Kutuzovsky, 32, 21', '+7(903)777-7777'),
       ('4f9a97c4-8300-11ee-b962-0242ac120011', '4515 193232', 'Polina', 'Petrova', 40, 'polinaPetrova@mail.ru',
        'WOMAN', 'Russia, Moscow, Kutuzovsky, 31, 21', '+7(900)888-8888'),
       ('4f9a97c4-8300-11ee-b962-0242ac120012', '4515 193232', 'Maks', 'Smirnov', 24, 'maksSmirnov@qwerty.org', 'MAN',
        'Russia, Moscow, Kutuzovsky, 35, 21', '+7(903)123-1234');

--changeset divantsov:4
INSERT INTO accounts
VALUES ('4f9a97c4-8300-11ee-b962-0242ac120001', '12345678910111213150', 'RUR', 1000,
        '4f9a97c4-8300-11ee-b962-0242ac120010'),
       ('4f9a97c4-8300-11ee-b962-0242ac120002', '12345678910111213149', 'USD', 100,
        '4f9a97c4-8300-11ee-b962-0242ac120010'),
       ('4f9a97c4-8300-11ee-b962-0242ac120003', '12345678910111213148', 'RUR', 50,
        '4f9a97c4-8300-11ee-b962-0242ac120011'),
       ('4f9a97c4-8300-11ee-b962-0242ac120004', '12345678910111213147', 'USD', 0.2,
        '4f9a97c4-8300-11ee-b962-0242ac120012');
