package com.bank.management.challenge.domain.ports.repositories;

import com.bank.management.challenge.infrastructure.entities.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClientRepository {

    Client save(Client client);

    Optional<Client> findById(UUID id);

    Client findByName(String name);

    List<Client> findAll();

    void delete(Client client);

}
