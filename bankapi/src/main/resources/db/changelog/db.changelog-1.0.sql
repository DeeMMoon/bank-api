--liquibase formatted sql

--changeset divantsov:1
CREATE TABLE clients
(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    first_name varchar(20) NOT NULL,
    last_name varchar(20) NOT NULL,
    age integer NOT NULL,
    email varchar(100) NOT NULL,
    gender varchar (10) NOT NULL,
    address varchar (200) NOT NULL,
    phone varchar (20)
);
--changeset divantsov:2
CREATE TABLE accounts
(
    id            UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    number        varchar(20),
    currency_type varchar(3),
    amount        decimal,
    is_blocked    boolean,
    client_id     uuid,
    foreign key (client_id) references clients (id)

);

