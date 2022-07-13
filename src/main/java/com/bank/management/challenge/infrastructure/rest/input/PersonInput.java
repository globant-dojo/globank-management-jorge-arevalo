package com.bank.management.challenge.infrastructure.rest.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PersonInput {

    @NotEmpty
    @Size(min = 2, max = 100)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 10)
    private String gender;

    private Integer age;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String identification;

    @NotEmpty
    @Size(min = 10, max = 100)
    private String address;

    @NotEmpty
    @Size(min = 5, max = 20)
    private String phoneNumber;

}
