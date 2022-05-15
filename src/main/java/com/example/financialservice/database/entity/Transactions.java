package com.example.financialservice.database.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="account_id", nullable=false)
    private Account account;

    private Integer transactions;
}
