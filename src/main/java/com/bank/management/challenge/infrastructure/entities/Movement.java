package com.bank.management.challenge.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Entity(name = "movement")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    @Column(name = "movement_date", nullable = false)
    private Date movementDate;

    @Column(name = "movement_type", nullable = false, length = 20)
    private String movementType;

    @Column(name = "value", nullable = false, columnDefinition = "double default 0.0")
    private Double value;

    @Column(name = "balance", nullable = false, columnDefinition = "double default 0.0")
    private Double balance;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "account_id", nullable = false, insertable = false, updatable = false)
    private Account account;

}
