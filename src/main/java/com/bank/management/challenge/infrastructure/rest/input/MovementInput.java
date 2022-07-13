package com.bank.management.challenge.infrastructure.rest.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class MovementInput {

    private Date movementDate;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String movementType;

    @NotNull
    private Double value;

    private  Double balance;

    @Valid
    private AccountInput accountInput;

}
