package com.github.kolizey72.riverbank.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_number", referencedColumnName = "number")
    @Getter @Setter
    private Account account;

    @Column(name = "amount")
    @Getter @Setter
    private Long amount;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private OperationType type;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter
    private LocalDateTime dateTime;
}
