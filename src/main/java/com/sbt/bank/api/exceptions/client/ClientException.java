package com.sbt.bank.api.exceptions.client;

public class ClientException extends RuntimeException{
    public ClientException(String message) {
        super(message);
    }
}
