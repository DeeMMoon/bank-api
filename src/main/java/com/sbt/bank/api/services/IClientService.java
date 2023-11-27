package com.sbt.bank.api.services;

import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;

import java.util.List;
import java.util.UUID;

/**
 * Интерфей сервиса для работы с клиентами
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Client
 * @see Account
 * @see Client
 * @see ClientDTO
 * @see com.sbt.bank.api.models.ClientInfo
 */
public interface IClientService {
    /**
     * Метод поиска клиента по его ID
     *
     * @param id Уникальный {@link Client#getId() номер клиента} типа {@link UUID}
     * @return {@link Client Клиент} с переданным id
     */
    Client findClientById(UUID id);

    /**
     * Метод получения счетов клиента по его ID
     *
     * @param id Уникальный {@link Client#getId() номер клиента} типа {@link UUID}
     * @return Список {@link Account счётов} {@link Client клиента} с переданным id
     */
    List<Account> getAccountsByClientId(UUID id);

    /**
     * Метод создания нового клиента
     *
     * @param clientDTO {@link ClientDTO DTO класс} с информацией о клиенте {@link com.sbt.bank.api.models.ClientInfo}
     * @return Созданный клиент с переданной информацией
     */
    Client createClient(ClientDTO clientDTO);

    /**
     * Обновление информации о клиента
     *
     * @param clientId  Уникальный номер клиента типа {@link UUID}
     * @param clientDTO {@link ClientDTO} DTO класс с новой информацией о клиенте {@link com.sbt.bank.api.models.ClientInfo}
     * @return Клиент с обновленной информацией
     */
    Client updateClientPersonalInformation(UUID clientId, ClientDTO clientDTO);
}
