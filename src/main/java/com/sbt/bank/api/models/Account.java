package com.sbt.bank.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сущность счёта в банке
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Currency
 * @see Client
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @UuidGenerator
    private UUID id;

    /**
     * Уникальный номер счета, состоящий из 20 цифр
     */
    @Column(name = "number")
    @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
    @NotBlank(message = "The account number must not be blank")
    @Size(min = 20, max = 20, message = "The length of the account number must be 20 digits")
    private String accountNumber;

    /**
     * {@link Currency Валюта} счёта
     */
    @Column(name = "currency_type")
    @NotNull(message = "Currency must not be null")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    /**
     * Баланс счета
     */
    @Column(name = "amount", columnDefinition = "Default '0.00'")
    @DecimalMin(value = "0.00")
    @NotNull(message = "Amount must not be null")
    private BigDecimal amount;

    /**
     * Флаг на то является ли счёт заблокированным
     */
    @Column(name = "is_blocked")
    @NotNull(message = "Account status must not be null")
    private Boolean isBlocked;

    /**
     * {@link Client Владелиц} счёта
     */
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
