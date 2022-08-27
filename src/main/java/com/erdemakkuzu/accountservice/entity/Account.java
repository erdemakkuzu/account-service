package com.erdemakkuzu.accountservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_full_name")
    private String customerFullName;

    @Column(name = "created_date")
    private Date createdDate;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    List<CurrencyAccount> currencyAccounts;
}
