package com.bank.management.challenge.infrastructure.repositories;

import com.bank.management.challenge.domain.ports.repositories.IMovementRepository;
import com.bank.management.challenge.infrastructure.entities.Movement;
import com.bank.management.challenge.infrastructure.repositories.jpa.MovementJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MovementRepository implements IMovementRepository {

    @Autowired
    MovementJpa movementJpa;

    @Override
    public Movement save(Movement movement) {
        return movementJpa.save(movement);
    }

    @Override
    public Optional<Movement> findById(UUID id) {
        return movementJpa.findById(id);
    }

    @Override
    public List<Movement> findAll() {
        return movementJpa.findAll();
    }

    @Override
    public void delete(Movement movement) {
        movementJpa.delete(movement);
    }

}
