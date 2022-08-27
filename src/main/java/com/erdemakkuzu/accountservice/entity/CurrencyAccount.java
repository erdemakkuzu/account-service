package com.erdemakkuzu.accountservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "currency_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyAccount {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    Account account;

    @Column(name = "balance")
    Double balance;

}
