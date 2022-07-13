package com.bank.management.challenge.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name = "client")
@DiscriminatorValue("client")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends Person {

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "status", nullable = false, columnDefinition = "boolean default true")
    private Boolean status;

}
