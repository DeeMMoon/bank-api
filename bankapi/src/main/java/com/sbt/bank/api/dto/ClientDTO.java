package com.sbt.bank.api.dto;
import com.sbt.bank.api.models.Client;

public record ClientDTO (){

    public static ClientDTO map(Client client){
        return new ClientDTO();
    }
}
