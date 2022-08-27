package com.erdemakkuzu.accountservice.service.impl;

import com.erdemakkuzu.accountservice.entity.Account;
import com.erdemakkuzu.accountservice.entity.Currency;
import com.erdemakkuzu.accountservice.entity.CurrencyAccount;
import com.erdemakkuzu.accountservice.exception.*;
import com.erdemakkuzu.accountservice.model.*;
import com.erdemakkuzu.accountservice.repository.AccountRepository;
import com.erdemakkuzu.accountservice.repository.CurrencyAccountRepository;
import com.erdemakkuzu.accountservice.service.AccountService;
import com.erdemakkuzu.accountservice.service.CurrencyService;
import com.erdemakkuzu.accountservice.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    CurrencyAccountRepository currencyAccountRepository;
    CurrencyService currencyService;

    @Autowired
    AccountServiceImpl(AccountRepository accountRepository,
                       CurrencyAccountRepository currencyAccountRepository,
                       CurrencyService currencyService) {
        this.accountRepository = accountRepository;
        this.currencyAccountRepository = currencyAccountRepository;
        this.currencyService = currencyService;

    }

    @Override
    @Transactional
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {

        Currency currency = currencyService.getCurrency(createAccountRequest.getInitialCurrency());

        if (currency == null) {
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

    @Override
    @Transactional
    public CreateCurrencyAccountResponse createCurrencyAccount(Long accountId, CreateCurrencyAccountRequest createCurrencyAccountRequest) {
        Optional<Account> account = accountRepository.findById(accountId);

        Currency currency =
                validateCreateCurrencyAccountRequestAndGetCurrency(accountId, createCurrencyAccountRequest, account);

        CurrencyAccount currencyAccount = new CurrencyAccount();
        currencyAccount.setAccount(account.get());
        currencyAccount.setCurrency(currency);
        currencyAccount.setBalance(0.0);

        currencyAccountRepository.saveAndFlush(currencyAccount);

        return MapperUtils.mapToCreateCurrencyAccountResponse(currencyAccount, createCurrencyAccountRequest);


    }

    private Currency validateCreateCurrencyAccountRequestAndGetCurrency(Long accountId,
                                                                        CreateCurrencyAccountRequest createCurrencyAccountRequest,
                                                                        Optional<Account> account) {
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        Currency currency = currencyService.getCurrency(createCurrencyAccountRequest.getCurrencyCode());

        if (currency == null) {
            throw new InvalidCurrencyException(createCurrencyAccountRequest.getCurrencyCode());
        }

        if (isCurrencyAccountAlreadyExists(account, currency)) {
            throw new CurrencyAccountAlreadyExistsException(createCurrencyAccountRequest.getCurrencyCode());
        }

        return currency;
    }

    private boolean isCurrencyAccountAlreadyExists(Optional<Account> account, Currency currency) {
        return account.get().getCurrencyAccounts()
                .stream().anyMatch(currencyAccount -> currencyAccount.getCurrency().getCode().equals(currency.getCode()));
    }

    @Override
    public PerformTransactionResponse performAddMoney(Long accountId, PerformTransactionRequest performTransactionRequest) {

        Double transactionAmount = performTransactionRequest.getAmount();

        if (transactionAmount < 1.0) {
            throw new MinimumTransactionAmountException(transactionAmount);
        }

        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        Currency currency = currencyService.getCurrency(performTransactionRequest.getCurrencyCode());

        if (currency == null) {
            throw new InvalidCurrencyException(performTransactionRequest.getCurrencyCode());
        }

        CurrencyAccount currencyAccount =
                validateAndGetCurrencyAccount(account.get(), performTransactionRequest.getCurrencyCode());

        currencyAccount.setBalance(currencyAccount.getBalance() + transactionAmount);

        currencyAccountRepository.saveAndFlush(currencyAccount);

        return MapperUtils.mapToPerformTransactionResponse(currencyAccount);

    }

    private CurrencyAccount validateAndGetCurrencyAccount(Account account, String currencyCode) {
        Optional<CurrencyAccount> currencyAccountOptional =
                account.getCurrencyAccounts().stream()
                        .filter(currencyAccount -> currencyAccount.getCurrency().getCode().equals(currencyCode)).findFirst();

        if (currencyAccountOptional.isEmpty()) {
            throw new CurrencyAccountNotFoundException(currencyCode);
        }

        return currencyAccountOptional.get();
    }

}
