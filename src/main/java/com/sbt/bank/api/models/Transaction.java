package com.sbt.bank.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность транзакции (данные о переводе средств между клиентами)
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Currency
 * @see TransactionStatus
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    /**
     * {@link UUID Уникальный идентификатор} в базе данных
     */
    @Id
    @UuidGenerator
    private UUID id;

    /**
     * {@link Account#accountNumber Номер счета} отправителя
     */
    @NotBlank(message = "Sender's account number must not be blank")
    @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
    @Column(name = "number_sender")
    private String accountNumberSender;

    /**
     * {@link Account#accountNumber Номер счета} получателя
     */
    @NotBlank(message = "Recipient's account number must not be blank")
    @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
    @Column(name = "number_recipient")
    private String accountNumberRecipient;

    /**
     * Сумма перевода
     */
    @Column(name = "amount", columnDefinition = "Decimal(10,2) default '0.00'")
    @DecimalMin(value = "0", inclusive = false, message = "Amount must be more 0.00")
    @DecimalMax(value = "1000000000.00", inclusive = false, message = "Amount must be less 1 000 000 000.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * {@link Currency Валюта} счета отправителя
     */
    @Column(name = "sender_cur")
    @NotNull(message = "Sender's currency type must not be null")
    @Enumerated(EnumType.STRING)
    private Currency currencyTypeSender;

    /**
     * {@link Currency Валюта} счета получателя
     */
    @Column(name = "recipient_cur")
    @NotNull(message = "Recipient's currency type must not be null")
    @Enumerated(EnumType.STRING)
    private Currency currencyTypeRecipient;

    /**
     * {@link LocalDateTime Время} начала транзакции
     */
    @NotNull(message = "Transaction start time must not be null")
    @Column(name = "start_time")
    private LocalDateTime sendingTime;

    /**
     * {@link LocalDateTime Время} изменения статуса транзакции
     */
    @NotNull(message = "Transaction current time must not be null")
    @Column(name = "current_status_time")
    private LocalDateTime updatedStatusTime;

    /**
     * {@link TransactionStatus Статус} транзакции
     */
    @Column(name = "status")
    @NotNull(message = "Transaction status must not be null")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
}
