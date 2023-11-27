package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

/**
 * Record DTO клиента с полем <b>clientInfo</b>
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Client
 */
@Schema(description = "Сущность личной информации клиента")
public record ClientDTO(@Valid ClientInfo clientInfo) {
    /**
     * Метод конвертации {@link Client клиента} в {@link ClientDTO}
     *
     * @param client клиент, которого нужно конвертировать
     * @return {@link ClientDTO}
     */
    public static ClientDTO map(Client client) {
        return new ClientDTO(client.getClientInfo());
    }

}