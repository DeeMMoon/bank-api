package com.sbt.bank.api.models;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность истории изменения информации о клиенте
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see ClientInfo
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "clients_history")
public class ClientModificationHistory {

    /**
     * Номер записи в таблице базы данных
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * {@link Client#id Id} клиента
     */
    @NotNull(message = "Client Id must not be null")
    private UUID clientId;

    /**
     * {@link ClientInfo Персональные данные} клиента
     */
    @Embedded
    private ClientInfo clientInfo;

    /**
     * {@link LocalDateTime Время} изменения информации о клиента
     */
    @NotNull(message = "Modified time must not be null")
    private LocalDateTime modifiedTime;
}
