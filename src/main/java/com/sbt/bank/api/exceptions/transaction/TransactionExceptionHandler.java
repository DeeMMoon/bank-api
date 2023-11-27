package com.sbt.bank.api.exceptions.transaction;

import com.sbt.bank.api.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Класс перехватчик исключений связанных с {@link com.sbt.bank.api.models.Transaction транзакциями}
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
@RestControllerAdvice
public class TransactionExceptionHandler {
    @ExceptionHandler(TransactionException.class)
    private ResponseEntity<ExceptionResponse> transactionExceptionHandler(TransactionException e) {
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionAmountException.class)
    private ResponseEntity<ExceptionResponse> transactionExceptionHandler(TransactionAmountException e) {
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionConversationCurrencyException.class)
    private ResponseEntity<ExceptionResponse> transactionExceptionHandler(TransactionConversationCurrencyException e) {
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    private ResponseEntity<ExceptionResponse> transactionExceptionHandler(TransactionNotFoundException e) {
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionStatusNotFoundException.class)
    private ResponseEntity<ExceptionResponse> transactionExceptionHandler(TransactionStatusNotFoundException e) {
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionValidationException.class)
    private ResponseEntity<ExceptionResponse> transactionExceptionHandler(TransactionValidationException e) {
        ExceptionResponse clientExceptionResponse = new ExceptionResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(clientExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
