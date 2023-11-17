package com.sbt.bank.api.exceptions.currency;

import com.sbt.bank.api.exceptions.ExceptionResponse;
import com.sbt.bank.api.exceptions.transaction.IllegalTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CurrencyExceptionHandler {
    @ExceptionHandler(CurrencyException.class)
    private ResponseEntity<ExceptionResponse> currencyExceptionHandler(CurrencyException e){
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
