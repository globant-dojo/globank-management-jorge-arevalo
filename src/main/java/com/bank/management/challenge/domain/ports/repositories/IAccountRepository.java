package com.bank.management.challenge.domain.ports.repositories;

import com.bank.management.challenge.infrastructure.entities.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAccountRepository {

    Account save (Account account);

    Optional<Account> findById(UUID id);

    Account getReferenceByAccountNumber(String accountNumber);

    List<Account> findByClientId(UUID clientId);

    List<Account> findAll();

    void delete(Account account);

}
