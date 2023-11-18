package com.sbt.bank.api.exceptions.currency.rate;

import com.sbt.bank.api.exceptions.ExceptionResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CurrencyRateExceptionHandler {
    @ExceptionHandler(CurrencyRateException.class)
    private ResponseEntity<ExceptionResponse> currencyRateExceptionHandler(CurrencyRateException e){
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyRateNotFoundException.class)
    private ResponseEntity<ExceptionResponse> currencyRateExceptionHandler(CurrencyRateNotFoundException e){
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.NOT_FOUND);
    }
}
