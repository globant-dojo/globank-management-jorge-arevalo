package com.bank.management.challenge.domain.usecases;

import com.bank.management.challenge.domain.models.AccountDto;
import com.bank.management.challenge.domain.models.MovementDto;
import com.bank.management.challenge.domain.ports.repositories.IAccountRepository;
import com.bank.management.challenge.domain.ports.repositories.IMovementRepository;
import com.bank.management.challenge.domain.ports.services.IMovementService;
import com.bank.management.challenge.infrastructure.config.exception.NotAvailableBalance;
import com.bank.management.challenge.infrastructure.config.exception.NotFoundException;
import com.bank.management.challenge.infrastructure.entities.Account;
import com.bank.management.challenge.infrastructure.entities.Movement;
import com.bank.management.challenge.infrastructure.rest.input.MovementInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MovementService implements IMovementService {

    @Autowired
    IMovementRepository movementRepository;

    @Autowired
    IAccountRepository accountRepository;

    @Override
    public MovementDto save(MovementInput movementInput) {
        Account account = accountRepository.getReferenceByAccountNumber(movementInput.getAccountNumber());
        if(account == null) {
            throw new NotFoundException("Account not found - Account number: ".concat(movementInput.getAccountNumber()));
        }
        Movement movement = Movement.builder()
                .movementDate(new Date())
                .movementType(movementInput.getMovementType())
                .value(movementInput.getValue())
                .build();
        calculateBalance(movement, account);
        account = accountRepository.save(account);
        movement.setAccount(account);
        return loadMovementData(movementRepository.save(movement));
    }

    @Override
    @Transactional(readOnly = true)
    public MovementDto findById(String id) {
        Optional<Movement> movementOptional = movementRepository.findById(UUID.fromString(id));
        if(movementOptional.isEmpty()) {
            throw new NotFoundException("Movement not found - Id # ".concat(id));
        }
        return loadMovementData(movementOptional.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementDto> findAll() {
        List<Movement> movementList = movementRepository.findAll();
        if(movementList.isEmpty()) {
            throw new NotFoundException("Movements not found");
        }
        List<MovementDto> movementDtoList = new ArrayList<>();
        for(Movement movement : movementList) {
            movementDtoList.add(loadMovementData(movement));
        }
        return movementDtoList;
    }

    @Override
    public MovementDto update(String id, MovementInput movementInput) {
        Optional<Movement> movementOptional = movementRepository.findById(UUID.fromString(id));
        if(movementOptional.isEmpty()) {
            throw new NotFoundException("Movement not found - Id # ".concat(id));
        }
        Movement movement = movementOptional.get();
        Optional<Account> accountOptional = accountRepository.findById(movementOptional.get().getId());
        if(accountOptional.isEmpty()) {
            throw new NotFoundException("Account not found - Id # ".concat(movementOptional.get().getId().toString()));
        }
        Account account = accountOptional.get();
        calculateRollBackBalance(movement, account);
        movement.setMovementType(movementInput.getMovementType());
        movement.setValue(movementInput.getValue());
        calculateBalance(movement, account);
        account = accountRepository.save(account);
        movement.setAccount(account);
        return loadMovementData(movementRepository.save(movement));
    }

    @Override
    public void delete(String id) {
        Optional<Movement> movementOptional = movementRepository.findById(UUID.fromString(id));
        if(movementOptional.isEmpty()) {
            throw new NotFoundException("Movement not found - Id # ".concat(id));
        }
        Movement movement = movementOptional.get();
        Optional<Account> accountOptional = accountRepository.findById(movement.getAccount().getId());
        Account account = accountOptional.get();
        calculateRollBackBalance(movement, account);
        account = accountRepository.save(account);
        movementRepository.delete(movementOptional.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementDto> findByMovementDateBetween(Date initialDate, Date finalDate) {
        List<Movement> movementList = movementRepository.findByMovementDateBetween(initialDate, finalDate);
        if(movementList.isEmpty()) {
            throw new NotFoundException("Movements not found");
        }
        List<MovementDto> movementDtoList = new ArrayList<>();
        for(Movement movement : movementList) {
            movementDtoList.add(loadMovementData(movement));
        }
        return movementDtoList;
    }

    private MovementDto loadMovementData(Movement movement) {
        return MovementDto.builder()
                .id(movement.getId())
                .movementDate(movement.getMovementDate())
                .movementType(movement.getMovementType())
                .value(movement.getValue())
                .balance(movement.getBalance())
                .accountId(movement.getAccount().getId())
                .build();
    }

    private void calculateBalance(Movement movement, Account account) {
        if (movement.getMovementType().equals("Debit")) {
            if(account.getInitialBalance() > 0D && account.getInitialBalance() >= movement.getValue()) {
                movement.setBalance(account.getInitialBalance() - movement.getValue());
            }
            else {
                throw new NotAvailableBalance("Not available balance");
            }
        } else if(movement.getMovementType().equals("Credit")) {
            movement.setBalance(account.getInitialBalance() + movement.getValue());
        }
        account.setInitialBalance(movement.getBalance());
    }

    private void calculateRollBackBalance(Movement movement, Account account) {
        if (movement.getMovementType().equals("Debit")) {
            movement.setBalance(account.getInitialBalance() + movement.getValue());
        } else if(movement.getMovementType().equals("Credit")) {
            movement.setBalance(account.getInitialBalance() - movement.getValue());
        }
        account.setInitialBalance(movement.getBalance());
    }

}
