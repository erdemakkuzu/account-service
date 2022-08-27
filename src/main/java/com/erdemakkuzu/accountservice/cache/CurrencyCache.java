package com.erdemakkuzu.accountservice.cache;

import com.erdemakkuzu.accountservice.entity.Currency;
import com.erdemakkuzu.accountservice.repository.CurrencyRepository;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class CurrencyCache {


    private final CurrencyRepository currencyRepository;

    Cache<String, Currency> currencyMap;

    @Autowired
    CurrencyCache(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    @PostConstruct
    public void init() {
        currencyMap = new Cache2kBuilder<String, Currency>() {}
                .name("currencyCache")
                .eternal(true)
                .entryCapacity(15)
                .build();

        List<Currency> currencies = currencyRepository.findAll();

        for(Currency currency : currencies){
            currencyMap.put(currency.getCode(), currency);
        }

    }

    public Cache<String, Currency> getCurrencyMap(){
        return currencyMap;
    }



}
