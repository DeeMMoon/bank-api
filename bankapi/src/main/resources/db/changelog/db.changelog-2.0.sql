--liquibase formatted sql

--changeset divantsov:1
INSERT INTO clients
VALUES ('4f9a97c4-8300-11ee-b962-0242ac120010', 'Ivan', 'Ivanov', 30, 'ivanovIvan@gmail.com', 'MAN', 'Russia, Moscow, Kutuzovsky, 32, 21', '+7(903)777-7777'),
       ('4f9a97c4-8300-11ee-b962-0242ac120011', 'Polina', 'Petrova', 40, 'polinaPetrova@mail.ru', 'WOMAN', 'Russia, Moscow, Kutuzovsky, 31, 21', '+7(900)888-8888'),
       ('4f9a97c4-8300-11ee-b962-0242ac120012', 'Maks', 'Smirnov', 24, 'maksSmirnov@qwerty.org', 'MAN', 'Russia, Moscow, Kutuzovsky, 35, 21', '+7(903)123-1234');

--changeset divantsov:2
INSERT INTO accounts
VALUES ('4f9a97c4-8300-11ee-b962-0242ac120001', '12345678910111213150', 'RUR', 1000, false,
        '4f9a97c4-8300-11ee-b962-0242ac120010'),
       ('4f9a97c4-8300-11ee-b962-0242ac120002', '12345678910111213149', 'USD', 100, false,
        '4f9a97c4-8300-11ee-b962-0242ac120010'),
       ('4f9a97c4-8300-11ee-b962-0242ac120003', '12345678910111213148', 'RUR', 50, false,
        '4f9a97c4-8300-11ee-b962-0242ac120011'),
       ('4f9a97c4-8300-11ee-b962-0242ac120004', '12345678910111213147', 'USD', 0.2, false,
        '4f9a97c4-8300-11ee-b962-0242ac120012');
