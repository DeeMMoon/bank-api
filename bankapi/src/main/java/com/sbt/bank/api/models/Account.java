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
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "accounts")
public class Account{
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "number")
    @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
    @NotBlank
    private String accountNumber;

    @Column(name = "currency_type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "amount",  columnDefinition="Default '0.00'")
    @DecimalMin(value = "0.0")
    @NotNull
    private BigDecimal amount;

    @Column(name = "is_blocked")
    @NotNull
    private Boolean isBlocked;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private Client client;
}
