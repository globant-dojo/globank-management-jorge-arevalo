package com.bank.management.challenge.domain.usecases;

import com.bank.management.challenge.domain.models.AccountDto;
import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.domain.ports.repositories.IAccountRepository;
import com.bank.management.challenge.domain.ports.repositories.IClientRepository;
import com.bank.management.challenge.domain.ports.services.IAccountService;
import com.bank.management.challenge.infrastructure.config.exception.NotFoundException;
import com.bank.management.challenge.infrastructure.entities.Account;
import com.bank.management.challenge.infrastructure.entities.Client;
import com.bank.management.challenge.infrastructure.rest.input.AccountInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AccountService implements IAccountService {

    @Autowired
    IAccountRepository accountRepository;

    @Autowired
    IClientRepository clientRepository;

    @Override
    public AccountDto save(AccountInput accountInput) {
        Client client = clientRepository.getReferenceByName(accountInput.getClientName());
        if(client == null) {
            throw new NotFoundException("Client not found - Name: ".concat(accountInput.getClientName()));
        }
        Account account = Account.builder()
                .accountNumber(accountInput.getAccountNumber())
                .accountType(accountInput.getAccountType())
                .initialBalance(accountInput.getInitialBalance())
                .status(accountInput.getStatus())
                .client(client)
                .build();
        return loadAccountData(accountRepository.save(account));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto findById(String id) {
        Optional<Account> accountOptional = accountRepository.findById(UUID.fromString(id));
        if(accountOptional.isEmpty()) {
            throw new NotFoundException("Account not found - Id # ".concat(id));
        }
        return loadAccountData(accountOptional.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> findAll() {
        List<Account> accountList = accountRepository.findAll();
        if(accountList.isEmpty()) {
            throw new NotFoundException("Accounts not found");
        }
        List<AccountDto> accountDtoList = new ArrayList<>();
        for(Account account : accountList) {
            accountDtoList.add(loadAccountData(account));
        }
        return accountDtoList;
    }

    @Override
    public AccountDto update(String id, AccountInput accountInput) {
        Optional<Account> accountOptional = accountRepository.findById(UUID.fromString(id));
        if(accountOptional.isEmpty()) {
            throw new NotFoundException("Account not found - Id # ".concat(id));
        }
        Account account = accountOptional.get();
        account.setAccountNumber(accountInput.getAccountNumber());
        account.setAccountType(accountInput.getAccountType());
        account.setInitialBalance(accountInput.getInitialBalance());
        account.setStatus(accountInput.getStatus());
        return loadAccountData(accountRepository.save(account));
    }

    @Override
    public void delete(String id) {
        Optional<Account> accountOptional = accountRepository.findById(UUID.fromString(id));
        if(accountOptional.isEmpty()) {
            throw new NotFoundException("Account not found - Id # ".concat(id));
        }
        accountRepository.delete(accountOptional.get());
    }

    private AccountDto loadAccountData(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .clientId(account.getClient().getId())
                .status(account.getStatus())
                .build();
    }

}
