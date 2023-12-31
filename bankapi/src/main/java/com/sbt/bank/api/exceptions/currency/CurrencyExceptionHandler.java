package com.sbt.bank.api.exceptions.currency;

import com.sbt.bank.api.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Класс перехватчик исключений связанных с {@link com.sbt.bank.api.models.Currency валютами}
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
@RestControllerAdvice
public class CurrencyExceptionHandler {
    @ExceptionHandler(CurrencyException.class)
    private ResponseEntity<ExceptionResponse> currencyExceptionHandler(CurrencyException e) {
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    private ResponseEntity<ExceptionResponse> currencyExceptionHandler(CurrencyNotFoundException e) {
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.NOT_FOUND);
    }
}
