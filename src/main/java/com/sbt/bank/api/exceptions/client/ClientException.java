package com.sbt.bank.api.exceptions.client;

/**
 * Родительский класс исключений связанных с {@link com.sbt.bank.api.models.Client клиентами}
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
public class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }
}
