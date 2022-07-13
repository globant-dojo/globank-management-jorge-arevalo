package com.bank.management.challenge.infrastructure.repositories.jpa;

import com.bank.management.challenge.infrastructure.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientJpa extends JpaRepository<Client, UUID> {

    List<Client> findByName(String name);

}
