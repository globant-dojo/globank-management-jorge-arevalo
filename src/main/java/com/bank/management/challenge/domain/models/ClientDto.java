package com.bank.management.challenge.domain.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ClientDto {

    private UUID id;
    private String password;
    private String name;
    private String gender;
    private Integer age;
    private String identification;
    private String address;
    private String phoneNumber;
    private Boolean status;

}
