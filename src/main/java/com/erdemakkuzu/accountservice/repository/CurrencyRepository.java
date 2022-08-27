package com.erdemakkuzu.accountservice.repository;

import com.erdemakkuzu.accountservice.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findFirstByCode(String code);

}
