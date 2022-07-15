package com.bank.management.challenge.domain.usecases;

import com.bank.management.challenge.domain.models.AccountDto;
import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.domain.ports.repositories.IAccountRepository;
import com.bank.management.challenge.domain.ports.repositories.IClientRepository;
import com.bank.management.challenge.domain.ports.repositories.IMovementRepository;
import com.bank.management.challenge.domain.ports.services.IReportService;
import com.bank.management.challenge.infrastructure.config.exception.NotFoundException;
import com.bank.management.challenge.infrastructure.entities.Account;
import com.bank.management.challenge.infrastructure.entities.Client;
import com.bank.management.challenge.infrastructure.entities.Movement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReportService implements IReportService {

    @Autowired
    IMovementRepository movementRepository;

    @Autowired
    IAccountRepository accountRepository;

    @Autowired
    IClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public ClientDto getReferenceByName(String clientName) {
        Client client = clientRepository.getReferenceByName(clientName);
        if(client == null) {
            throw new NotFoundException("Client not found - Name: ".concat(clientName));
        }
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> findByClientId(UUID clientId) {
        List<Account> accountList = accountRepository.findByClientId(clientId);
        if(accountList.isEmpty()) {
            throw new NotFoundException("Accounts not found - Client Id: ".concat(clientId.toString()));
        }
        List<AccountDto> accountDtoList = new ArrayList<>();
        for(Account account : accountList) {
            AccountDto accountDto = AccountDto.builder()
                    .id(account.getId())
                    .accountNumber(account.getAccountNumber())
                    .accountType(account.getAccountType())
                    .initialBalance(account.getInitialBalance())
                    .status(account.getStatus())
                    .build();
            accountDtoList.add(accountDto);
        }
        return accountDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movement> findByAccountNumber(String accountNumber) {
        return null;
    }

}
