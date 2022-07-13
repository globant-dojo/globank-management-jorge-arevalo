package com.bank.management.challenge.domain.ports.services;

import com.bank.management.challenge.domain.models.PersonDto;
import com.bank.management.challenge.infrastructure.rest.input.PersonInput;

import java.util.List;

public interface IPersonService {

    PersonDto save(PersonInput personInput);

    PersonDto findById(String id);

}
