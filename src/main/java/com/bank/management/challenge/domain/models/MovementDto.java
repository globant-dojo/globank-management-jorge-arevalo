package com.bank.management.challenge.domain.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MovementDto {

    private UUID id;
    private Date movementDate;
    private String movementType;
    private Double value;
    private Double balance;
    private UUID accountId;

}
