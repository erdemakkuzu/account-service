package com.erdemakkuzu.accountservice.service.impl;

import com.erdemakkuzu.accountservice.cache.CurrencyCache;
import com.erdemakkuzu.accountservice.entity.Currency;
import com.erdemakkuzu.accountservice.exception.InvalidCurrencyException;
import com.erdemakkuzu.accountservice.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyCache currencyCache;

    @Autowired
    CurrencyServiceImpl(CurrencyCache currencyCache) {
        this.currencyCache = currencyCache;
    }

    @Override
    public Currency getCurrency(String currencyCode) {

        Currency currency = currencyCache.getCurrencyMap().get(currencyCode);

        if (currency == null) {
            throw new InvalidCurrencyException(currencyCode);
        }

        return currency;
    }
}
