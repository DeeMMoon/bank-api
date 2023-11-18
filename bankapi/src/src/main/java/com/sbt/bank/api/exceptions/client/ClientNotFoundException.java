package com.sbt.bank.api.exceptions.client;

public final class ClientNotFoundException extends ClientException{
    public ClientNotFoundException(String message) {
        super(message);
    }
}
