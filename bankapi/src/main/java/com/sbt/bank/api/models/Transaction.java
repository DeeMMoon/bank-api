package com.sbt.bank.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @UuidGenerator
    private UUID id;

    @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
    @NonNull
    @Column(name = "number_sender")
    private String accountNumberSender;

    @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
    @NonNull
    @Column(name = "number_recipient")
    private String accountNumberRecipient;

    @Column(name = "amount",  columnDefinition="Decimal(10,2) default '0.00'")
    @DecimalMin(value = "0.0")
    @Digits(integer=9, fraction=2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "sender_cur")
    @NonNull
    @Enumerated(EnumType.STRING)
    private Currency currencyTypeSender;

    @Column(name = "recipient_cur")
    @NonNull
    @Enumerated(EnumType.STRING)
    private Currency currencyTypeRecipient;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime sendingTime;

    @NotNull
    @Column(name = "current_status_time")
    private LocalDateTime enrollmentTime;

    @Column(name = "status")
    @NonNull
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
}
