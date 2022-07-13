package com.bank.management.challenge.domain.ports.services;

import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.infrastructure.rest.input.ClientInput;

import java.util.List;

public interface IClientService {

    ClientDto save(ClientInput clientInput);

    ClientDto findById(String id);

    List<ClientDto> findAll();

    ClientDto update(String id, ClientInput clientInput);

    void delete(String id);

}
