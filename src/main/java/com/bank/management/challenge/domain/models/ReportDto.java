package com.bank.management.challenge.domain.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ReportDto {

    private Date date;
    private String client;
    private String accountNumber;
    private String accountType;
    private Double initialBalance;
    private Boolean status;
    private Double movement;
    private Double availableBalance;

}
