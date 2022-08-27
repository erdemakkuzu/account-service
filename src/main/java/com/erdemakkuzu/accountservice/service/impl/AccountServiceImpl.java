package com.erdemakkuzu.accountservice.service.impl;

import com.erdemakkuzu.accountservice.cache.CurrencyCache;
import com.erdemakkuzu.accountservice.entity.Account;
import com.erdemakkuzu.accountservice.entity.Currency;
import com.erdemakkuzu.accountservice.entity.CurrencyAccount;
import com.erdemakkuzu.accountservice.repository.AccountRepository;
import com.erdemakkuzu.accountservice.repository.CurrencyAccountRepository;
import com.erdemakkuzu.accountservice.repository.CurrencyRepository;
import com.erdemakkuzu.accountservice.service.AccountService;
import com.erdemakkuzu.accountservice.model.CreateAccountRequest;
import com.erdemakkuzu.accountservice.model.CreateAccountResponse;
import com.erdemakkuzu.accountservice.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CurrencyAccountRepository currencyAccountRepository;

    @Autowired
    CurrencyCache currencyCache;

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {

        Currency currency = currencyCache.getCurrencyCache().get(createAccountRequest.getInitialCurrency());

        if(currency ==null ){
            throw new RuntimeException();
        }

        Account account = new Account();
        account.setCustomerFullName(createAccountRequest.getCustomerFullName());
        account.setCreatedDate(new Date());

        Account savedAccount = accountRepository.saveAndFlush(account);

        CurrencyAccount currencyAccount = new CurrencyAccount();
        currencyAccount.setAccount(savedAccount);
        currencyAccount.setCurrency(currency);
        currencyAccount.setBalance(0.0);

        currencyAccountRepository.saveAndFlush(currencyAccount);

        return MapperUtils.mapToCreateAccountResponse(currencyAccount);
    }
}
