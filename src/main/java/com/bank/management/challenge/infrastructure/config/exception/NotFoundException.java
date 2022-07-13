package com.bank.management.challenge.infrastructure.config.exception;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }
}
