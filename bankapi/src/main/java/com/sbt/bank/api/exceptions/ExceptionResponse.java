package com.sbt.bank.api.exceptions;

/**
 * Record для составления собственного ответа ошибки
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
public record ExceptionResponse(String message, Long timestamp) {
}
