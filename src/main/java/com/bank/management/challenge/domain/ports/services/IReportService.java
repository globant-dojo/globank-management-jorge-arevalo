package com.bank.management.challenge.domain.ports.services;

import com.bank.management.challenge.domain.models.AccountDto;
import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.infrastructure.entities.Movement;

import java.util.List;
import java.util.UUID;

public interface IReportService {

    ClientDto getReferenceByName(String clientName);

    List<AccountDto> findByClientId(UUID clientId);

    List<Movement> findByAccountNumber(String accountNumber);

}
