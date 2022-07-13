package com.bank.management.challenge.domain.ports.repositories;

import com.bank.management.challenge.infrastructure.entities.Person;

import java.util.Optional;
import java.util.UUID;

public interface IPersonRepository {

    Person save(Person person);

    Optional<Person> findById(UUID id);

}
