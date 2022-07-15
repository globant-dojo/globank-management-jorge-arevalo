package com.bank.management.challenge.domain.ports.repositories;

import com.bank.management.challenge.infrastructure.entities.Movement;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMovementRepository {

    Movement save(Movement movement);

    Optional<Movement> findById(UUID id);

    List<Movement> findAll();

    void delete(Movement movement);

    List<Movement> findByMovementDateBetween(Date initialDate, Date finalDate);

}
