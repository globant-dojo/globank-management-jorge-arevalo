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
public class MovementService implements IMovementService {

    @Autowired
    IMovementRepository movementRepository;

    @Autowired
    IAccountRepository accountRepository;

    @Override
    @Transactional
    public MovementDto save(MovementInput movementInput) {
        Account account = accountRepository.findByAccountNumber(movementInput.getAccountInput().getAccountNumber());
        if(account == null) {
            throw new NotFoundException("Account not found - Account number: ".concat(movementInput.getAccountInput().getAccountNumber()));
        }
        Movement movement = Movement.builder()
                .movementDate(new Date())
                .movementType(movementInput.getMovementType())
                .value(movementInput.getValue())
                .balance(calculateBalance(movementInput))
                .account(account)
                .build();
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
    @Transactional
    public MovementDto update(String id, MovementInput movementInput) {
        Optional<Movement> movementOptional = movementRepository.findById(UUID.fromString(id));
        if(movementOptional.isEmpty()) {
            throw new NotFoundException("Movement not found - Id # ".concat(id));
        }
        Movement movement = movementOptional.get();
        movement.setMovementDate(movementInput.getMovementDate());
        movement.setMovementType(movementInput.getMovementType());
        movement.setValue(movementInput.getValue());
        movement.setBalance(movementInput.getBalance());
        return loadMovementData(movementRepository.save(movement));
    }

    @Override
    @Transactional
    public void delete(String id) {
        Optional<Movement> movementOptional = movementRepository.findById(UUID.fromString(id));
        if(movementOptional.isEmpty()) {
            throw new NotFoundException("Movement not found - Id # ".concat(id));
        }
        movementRepository.delete(movementOptional.get());
    }

    private MovementDto loadMovementData(Movement movement) {
        AccountDto accountDto = AccountDto.builder()
                .id(movement.getAccount().getId())
                .accountNumber(movement.getAccount().getAccountNumber())
                .accountType(movement.getAccount().getAccountType())
                .initialBalance(movement.getAccount().getInitialBalance())
                .build();
        return MovementDto.builder()
                .id(movement.getId())
                .movementDate(movement.getMovementDate())
                .movementType(movement.getMovementType())
                .value(movement.getValue())
                .balance(movement.getBalance())
                .accountDto(accountDto)
                .build();
    }

    private Double calculateBalance(MovementInput movementInput) {
        Double newBalance = 0D;
        if (movementInput.getMovementType().equals("Debit")) {
            if(movementInput.getAccountInput().getInitialBalance() > 0D
                    && movementInput.getAccountInput().getInitialBalance() > movementInput.getValue()) {
                newBalance = movementInput.getAccountInput().getInitialBalance() - movementInput.getValue();
            }
            else {
                throw new NotAvailableBalance("Not available balance");
            }
        } else if(movementInput.getMovementType().equals("Credit")) {
            newBalance = movementInput.getAccountInput().getInitialBalance() + movementInput.getValue();
        }
        return newBalance;
    }

}
