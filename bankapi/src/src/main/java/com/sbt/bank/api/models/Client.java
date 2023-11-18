package com.sbt.bank.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Embedded
    private ClientInfo clientInfo;

    @NotNull
    private LocalDateTime createdTime;

    @NotNull
    private LocalDateTime lastModifiedTime;

    @OneToMany(mappedBy = "client")
    private List<Account> accounts;
}
