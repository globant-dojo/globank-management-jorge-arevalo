package com.bank.management.challenge.domain.usecases;

import com.bank.management.challenge.domain.models.PersonDto;
import com.bank.management.challenge.domain.ports.repositories.IPersonRepository;
import com.bank.management.challenge.domain.ports.services.IPersonService;
import com.bank.management.challenge.infrastructure.config.exception.NotFoundException;
import com.bank.management.challenge.infrastructure.entities.Person;
import com.bank.management.challenge.infrastructure.rest.input.PersonInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PersonService implements IPersonService {

    @Autowired
    IPersonRepository personRepository;

    @Override
    public PersonDto save(PersonInput personInput) {
        Person person = Person.builder()
                .name(personInput.getName())
                .gender(personInput.getGender())
                .age(personInput.getAge())
                .identification(personInput.getIdentification())
                .address(personInput.getAddress())
                .phoneNumber(personInput.getPhoneNumber())
                .build();
        return loadPersonData(personRepository.save(person));
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto findById(String id) {
        Optional<Person> personOptional =personRepository.findById(UUID.fromString(id));
        if(personOptional.isEmpty()) {
            throw new NotFoundException("Not found Id: ".concat(id.toString()));
        }
        return loadPersonData(personOptional.get());
    }

    private PersonDto loadPersonData(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .gender(person.getGender())
                .age(person.getAge())
                .identification(person.getIdentification())
                .address(person.getAddress())
                .phoneNumber(person.getPhoneNumber())
                .build();
    }

}
