package com.bank.management.challenge.domain.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AccountDto {

    private UUID id;
    private String accountNumber;
    private String accountType;
    private Double initialBalance;
    private Boolean status;
    private UUID clientId;

}
