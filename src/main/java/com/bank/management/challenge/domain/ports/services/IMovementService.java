package com.bank.management.challenge.domain.ports.services;

import com.bank.management.challenge.domain.models.MovementDto;
import com.bank.management.challenge.infrastructure.rest.input.MovementInput;

import java.util.Date;
import java.util.List;

public interface IMovementService {

    MovementDto save(MovementInput movementInput);

    MovementDto findById(String id);

    List<MovementDto> findAll();

    MovementDto update(String id, MovementInput movementInput);

    void delete(String id);

    List<MovementDto> findByMovementDateBetween(Date initialDate, Date finalDate);

}
