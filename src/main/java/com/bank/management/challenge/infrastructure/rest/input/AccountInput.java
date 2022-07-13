package com.bank.management.challenge.infrastructure.rest.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AccountInput {

    @NotEmpty
    @Size(min = 5, max = 20)
    private String accountNumber;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String accountType;

    @NotNull
    private Double initialBalance;

    private Boolean status;

    @Size(max = 100)
    private String clientName;

}
