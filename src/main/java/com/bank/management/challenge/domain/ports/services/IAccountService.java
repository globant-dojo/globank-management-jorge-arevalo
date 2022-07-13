package com.bank.management.challenge.domain.ports.services;

import com.bank.management.challenge.domain.models.AccountDto;
import com.bank.management.challenge.infrastructure.rest.input.AccountInput;

import java.util.List;

public interface IAccountService {

    AccountDto save(AccountInput accountInput);

    AccountDto findById(String id);

    List<AccountDto> findAll();

    AccountDto update(String id, AccountInput accountInput);

    void delete(String id);

}
