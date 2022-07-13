package com.bank.management.challenge.infrastructure.rest.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FormatInput<T> {

    @Valid
    @NotNull
    T data;

}
