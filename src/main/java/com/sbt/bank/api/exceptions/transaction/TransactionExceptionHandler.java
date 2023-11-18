package com.sbt.bank.api.exceptions.transaction;

import com.sbt.bank.api.exceptions.ExceptionResponse;
import com.sbt.bank.api.exceptions.client.ClientException;
import com.sbt.bank.api.exceptions.client.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {
    @ExceptionHandler(IllegalTransactionException.class)
    private ResponseEntity<ExceptionResponse> transactionExceptionHandler(IllegalTransactionException e){
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
