package com.sbt.bank.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Сущность клиент
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see ClientInfo
 * @see Account
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {

    /**
     * {@link UUID Уникальный идентификатор} клиента
     */
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    /**
     * {@link ClientInfo Персональные данные} клиентах
     */
    @Embedded
    private ClientInfo clientInfo;

    /**
     * {@link LocalDateTime Время} создания клиента
     */
    @NotNull(message = "Client created time must not be null")
    private LocalDateTime createdTime;

    /**
     * {@link LocalDateTime Время} последнего изменения личной информации клиента
     */
    @NotNull(message = "Last modified time must not be null")
    private LocalDateTime lastModifiedTime;

    /**
     * Список {@link Account счетов} клиента
     */
    @OneToMany(mappedBy = "client")
    private List<Account> accounts;
}
