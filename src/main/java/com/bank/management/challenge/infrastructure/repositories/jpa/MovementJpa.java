package com.bank.management.challenge.infrastructure.repositories.jpa;

import com.bank.management.challenge.infrastructure.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MovementJpa extends JpaRepository<Movement, UUID> {

    List<Movement> findByMovementDateBetween(Date initialDate, Date finalDate);

}
