package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientInfo;
import jakarta.validation.Valid;

public record ClientDTO(@Valid ClientInfo clientInfo) {

    public static ClientDTO map(Client client) {
        return new ClientDTO(client.getClientInfo());
    }

}