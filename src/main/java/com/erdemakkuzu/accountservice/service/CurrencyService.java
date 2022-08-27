package com.erdemakkuzu.accountservice.service;

import com.erdemakkuzu.accountservice.entity.Currency;
import org.springframework.stereotype.Service;

@Service
public interface CurrencyService {

    Currency getCurrency(String currencyCode);

}
