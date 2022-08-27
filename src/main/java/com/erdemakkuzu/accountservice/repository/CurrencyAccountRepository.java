package com.erdemakkuzu.accountservice.repository;

import com.erdemakkuzu.accountservice.entity.CurrencyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyAccountRepository extends JpaRepository<CurrencyAccount, Long> {
}
