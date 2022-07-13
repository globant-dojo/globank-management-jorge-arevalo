package com.bank.management.challenge.domain.usecases;

import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.domain.ports.repositories.IClientRepository;
import com.bank.management.challenge.domain.ports.services.IClientService;
import com.bank.management.challenge.infrastructure.config.exception.NotFoundException;
import com.bank.management.challenge.infrastructure.entities.Client;
import com.bank.management.challenge.infrastructure.rest.input.ClientInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService implements IClientService {

    @Autowired
    IClientRepository clientRepository;

    @Override
    public ClientDto save(ClientInput clientInput) {
        Client client = Client.builder()
                .name(clientInput.getName())
                .gender(clientInput.getGender())
                .age(clientInput.getAge())
                .identification(clientInput.getIdentification())
                .address(clientInput.getAddress())
                .phoneNumber(clientInput.getPhoneNumber())
                .password(clientInput.getPassword())
                .status(clientInput.getStatus())
                .build();
        return loadClientData(clientRepository.save(client));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findById(String id) {
        Optional<Client> clientOptional = clientRepository.findById(UUID.fromString(id));
        if(clientOptional.isEmpty()) {
            throw new NotFoundException("Client not found - Id # ".concat(id));
        }
        return loadClientData(clientOptional.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findAll() {
        List<Client> clientList = clientRepository.findAll();
        if(clientList.isEmpty()) {
            throw new NotFoundException("Clients not found");
        }
        List<ClientDto> clientDtoList = new ArrayList<>();
        for(Client client : clientList) {
            clientDtoList.add(loadClientData(client));
        }
        return clientDtoList;
    }

    @Override
    @Transactional
    public ClientDto update(String id, ClientInput clientInput) {
        Optional<Client> clientOptional = clientRepository.findById(UUID.fromString(id));
        if(clientOptional.isEmpty()) {
            throw new NotFoundException("Client not found - Id # ".concat(id));
        }
        Client client = clientOptional.get();
        client.setName(clientInput.getName());
        client.setGender(clientInput.getGender());
        client.setAge(clientInput.getAge());
        client.setIdentification(clientInput.getIdentification());
        client.setAddress(clientInput.getAddress());
        client.setPhoneNumber(clientInput.getPhoneNumber());
        client.setPassword(clientInput.getPassword());
        client.setStatus(clientInput.getStatus());
        return loadClientData(clientRepository.save(client));
    }

    @Override
    @Transactional
    public void delete(String id) {
        Optional<Client> clientOptional = clientRepository.findById(UUID.fromString(id));
        if(clientOptional.isEmpty()) {
            throw new NotFoundException("Client not found - Id # ".concat(id));
        }
        clientRepository.delete(clientOptional.get());
    }

    private ClientDto loadClientData(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .password(client.getPassword())
                .name(client.getName())
                .gender(client.getGender())
                .age(client.getAge())
                .identification(client.getIdentification())
                .address(client.getAddress())
                .phoneNumber(client.getPhoneNumber())
                .status(client.getStatus())
                .build();
    }

}
