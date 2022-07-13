package com.bank.management.challenge.infrastructure.repositories;

import com.bank.management.challenge.domain.ports.repositories.IPersonRepository;
import com.bank.management.challenge.infrastructure.entities.Person;
import com.bank.management.challenge.infrastructure.repositories.jpa.PersonJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PersonRepository implements IPersonRepository {

    @Autowired
    private PersonJpa personJpa;

    @Override
    public Person save(Person person) {
        return personJpa.save(person);
    }

    @Override
    public Optional<Person> findById(UUID id) {
        return personJpa.findById(id);
    }
}
