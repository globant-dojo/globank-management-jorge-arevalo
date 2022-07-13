package com.bank.management.challenge.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.UUID;

@Entity(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("person")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "identification", nullable = false, length = 20)
    private String identification;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

}
