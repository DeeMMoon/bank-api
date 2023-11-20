package com.sbt.bank.api.exceptions.account;

import com.sbt.bank.api.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionHandler{
    @ExceptionHandler(AccountException.class)
    private ResponseEntity<ExceptionResponse> accountExceptionHandler(AccountException e){
        ExceptionResponse accountExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(accountExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    private ResponseEntity<ExceptionResponse> accountExceptionHandler(AccountNotFoundException e){
        ExceptionResponse accountExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(accountExceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TooLongTimeGenerationAccountNumberException.class)
    private ResponseEntity<ExceptionResponse> accountExceptionHandler(TooLongTimeGenerationAccountNumberException e){
        ExceptionResponse accountExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(accountExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountIsBlockedException.class)
    private ResponseEntity<ExceptionResponse> accountExceptionHandler(AccountIsBlockedException e){
        ExceptionResponse accountExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(accountExceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
