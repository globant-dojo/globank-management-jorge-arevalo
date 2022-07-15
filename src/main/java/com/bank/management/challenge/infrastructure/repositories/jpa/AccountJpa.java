package com.bank.management.challenge.infrastructure.repositories.jpa;

import com.bank.management.challenge.infrastructure.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountJpa extends JpaRepository<Account, UUID> {

    Account getReferenceByAccountNumber(String accountNumber);

    List<Account> findByClientId(UUID clientId);

}
