package com.sbt.bank.api.exceptions.client;

import com.sbt.bank.api.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(ClientException.class)
    private ResponseEntity<ExceptionResponse> clientExceptionHandler(ClientException e){
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    private ResponseEntity<ExceptionResponse> clientExceptionHandler(ClientNotFoundException e){
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.NOT_FOUND);
    }
}
