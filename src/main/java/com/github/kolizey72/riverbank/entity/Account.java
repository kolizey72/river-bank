package com.github.kolizey72.riverbank.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long number;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Getter @Setter
    private User user;

    @Column(name = "amount")
    @Getter @Setter
    private Long amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private Currency currency;

    @OneToMany(mappedBy = "account")
    @Getter @Setter
    private List<Operation> operations;

    public Account() {
    }

    public Account(Long amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
