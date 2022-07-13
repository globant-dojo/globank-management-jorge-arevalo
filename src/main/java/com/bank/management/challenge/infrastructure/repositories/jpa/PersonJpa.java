package com.bank.management.challenge.infrastructure.repositories.jpa;

import com.bank.management.challenge.infrastructure.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonJpa extends JpaRepository<Person, UUID> {
}
