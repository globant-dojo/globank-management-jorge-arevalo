package com.bank.management.challenge.infrastructure.repositories;

import com.bank.management.challenge.domain.ports.repositories.IClientRepository;
import com.bank.management.challenge.infrastructure.entities.Client;
import com.bank.management.challenge.infrastructure.repositories.jpa.ClientJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ClientRepository implements IClientRepository {

    @Autowired
    ClientJpa clientJpa;

    @Override
    public Client save(Client client) {
        return clientJpa.save(client);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return clientJpa.findById(id);
    }

    @Override
    public List<Client> findAll() {
        return clientJpa.findAll();
    }

    @Override
    public void delete(Client client) {
        clientJpa.delete(client);
    }

    @Override
    public Client findByName(String name) {
        return clientJpa.findByName(name).get(0);
    }

}
