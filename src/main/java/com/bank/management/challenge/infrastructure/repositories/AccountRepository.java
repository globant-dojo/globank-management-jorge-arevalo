package com.bank.management.challenge.infrastructure.repositories;

import com.bank.management.challenge.domain.ports.repositories.IAccountRepository;
import com.bank.management.challenge.infrastructure.entities.Account;
import com.bank.management.challenge.infrastructure.repositories.jpa.AccountJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountRepository implements IAccountRepository {

    @Autowired
    AccountJpa accountJpa;

    @Override
    public Account save(Account account) {
        return accountJpa.save(account);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return accountJpa.findById(id);
    }

    @Override
    public Account findByAccountNumber(String accountNumber) { return accountJpa.findByAccountNumber(accountNumber).get(0); }

    @Override
    public List<Account> findAll() {
        return accountJpa.findAll();
    }

    @Override
    public void delete(Account account) {
        accountJpa.delete(account);
    }

}
