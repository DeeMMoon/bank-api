package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.exceptions.client.ClientCreateException;
import com.sbt.bank.api.exceptions.client.ClientNotFoundException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientInfo;
import com.sbt.bank.api.models.ClientModificationHistory;
import com.sbt.bank.api.repositories.ClientModificationHistoryRepository;
import com.sbt.bank.api.repositories.ClientRepository;
import com.sbt.bank.api.services.IClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Класс реализации сервиса по работе с клиентами
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Client
 * @see ClientRepository
 * @see ClientModificationHistoryRepository
 * @see ClientDTO
 * @see Account
 */
@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private final ClientModificationHistoryRepository clientModificationHistoryRepository;

    public ClientServiceImpl(ClientRepository clientRepository, ClientModificationHistoryRepository clientModificationHistoryRepository) {
        this.clientRepository = clientRepository;
        this.clientModificationHistoryRepository = clientModificationHistoryRepository;
    }

    /**
     * Метод поиска клиента по его ID.
     * В случае, если клиент не найден будет выброшено исключение со статусом ответа 404.
     *
     * @param id Уникальный {@link Client#getId() номер клиента} типа {@link UUID}
     * @return {@link Client Клиент} с переданным id
     */
    @Override
    public Client findClientById(UUID id) {
        return clientRepository
                .findClientById(id).
                orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + " not found"));
    }

    /**
     * Метод получения счетов клиента по его ID.
     * В случае, если клиент не найден будет выброшено исключение со статусом ответа 404.
     *
     * @param id Уникальный {@link Client#getId() номер клиента} типа {@link UUID}
     * @return Список {@link Account счётов} {@link Client клиента} с переданным id
     */
    @Override
    public List<Account> getAccountsByClientId(UUID id) {
        return clientRepository
                .findClientById(id).
                orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + " not found")).getAccounts();
    }

    /**
     * Метод создания нового клиента.
     * Если переданное в DTO поле personalID (эмуляция паспорта) совпадает с {@link ClientInfo#getPersonalID() personalID}
     * другого клиента, то будет выброшено исключение со статусом ответа 409.
     *
     * @param clientDTO {@link ClientDTO DTO класс} с информацией о клиенте {@link com.sbt.bank.api.models.ClientInfo}
     * @return Созданный клиент с переданной информацией
     */
    @Override
    @Transactional
    public Client createClient(ClientDTO clientDTO) {
        if (clientRepository.findClientByClientInfoPersonalID(clientDTO.clientInfo().getPersonalID()).isPresent()) {
            throw new ClientCreateException("It is not possible to create a profile because a client with such a passport has already been created");
        }
        Client client = new Client().builder()
                .id(UUID.randomUUID())
                .clientInfo(clientDTO.clientInfo())
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .accounts(Collections.EMPTY_LIST)
                .build();
        return clientRepository.save(client);
    }

    /**
     * Обновление информации о клиента.
     * При обновлении информации о клиенте проверяется наличие этого клиента.
     * Если клиента с переданным id нет, то будет выброшено исключение с кодом ответа 404.
     * Также проверяется уникальность personalID (аналог паспорта), если переданный personalID
     * установлен у другого пользователя, то будет выброшено исключение с кодом ответа 409.
     *
     * @param clientId  Уникальный номер клиента типа {@link UUID}
     * @param clientDTO {@link ClientDTO} DTO класс с новой информацией о клиенте {@link com.sbt.bank.api.models.ClientInfo}
     * @return Клиент с обновленной информацией
     */
    @Override
    @Transactional
    public Client updateClientPersonalInformation(UUID clientId, ClientDTO clientDTO) {
        var client = clientRepository.findClientById(clientId);
        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client with id: " + clientId + " not found");
        }
        var sameClient = clientRepository.findClientByClientInfoPersonalID(clientDTO.clientInfo().getPersonalID());
        if (sameClient.isPresent()) {
            if (!sameClient.get().getId().equals(clientId)) {
                throw new ClientCreateException("It is not possible to create a profile because a client with such a passport has already been created");
            }
        }
        var clientModificationHistory = new ClientModificationHistory().builder()
                .clientId(client.get().getId())
                .clientInfo(client.get().getClientInfo())
                .modifiedTime(LocalDateTime.now())
                .build();
        clientModificationHistoryRepository.save(clientModificationHistory);
        client.get().setClientInfo(clientDTO.clientInfo());
        client.get().setLastModifiedTime(LocalDateTime.now());
        clientRepository.save(client.get());
        return client.get();
    }

}
