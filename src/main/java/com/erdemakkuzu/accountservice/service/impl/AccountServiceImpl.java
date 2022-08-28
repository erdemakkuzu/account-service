package com.erdemakkuzu.accountservice.service.impl;

import com.erdemakkuzu.accountservice.constant.ExternalUrls;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    CurrencyAccountRepository currencyAccountRepository;
    CurrencyService currencyService;

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

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

        String initialCurrency = createAccountRequest.getInitialCurrency();

        Currency currency = currencyService.getCurrency(initialCurrency);

        if (currency == null) {
            throw new InvalidCurrencyException(initialCurrency);
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
    public CreateCurrencyAccountResponse createCurrencyAccount(Long accountId,
                                                               CreateCurrencyAccountRequest createCurrencyAccountRequest) {
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

    @Override
    @Transactional
    public PerformTransactionResponse performDebit(Long accountId, PerformTransactionRequest performTransactionRequest) {
        Double transactionAmount = validateAndGetTransactionAmount(performTransactionRequest.getAmount());

        Account account = validateAndGetAccount(accountId);

        validateCurrency(performTransactionRequest.getCurrencyCode());

        CurrencyAccount currencyAccount =
                validateAndGetCurrencyAccount(account, performTransactionRequest.getCurrencyCode());

        if (currencyAccount.getBalance() < transactionAmount) {
            throw new NotEnoughBalanceException(transactionAmount);
        }

        currencyAccount.setBalance(currencyAccount.getBalance() - transactionAmount);

        currencyAccountRepository.saveAndFlush(currencyAccount);

        new Thread(this::simulateExternalCall).start();

        return MapperUtils.mapToPerformTransactionResponse(currencyAccount);
    }

    @Override
    public GetAccountResponse getAccount(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        return MapperUtils.mapToGetAccountResponse(account.get());

    }

    @Override
    public PerformCurrencyExchangeResponse performCurrencyExchange(Long accountId,
                                                                   PerformCurrencyExchangeRequest performCurrencyExchangeRequest) {
        Currency fromCurrency = validateCurrency(performCurrencyExchangeRequest.getFrom());
        Currency toCurrency = validateCurrency(performCurrencyExchangeRequest.getTo());

        Double amount = validateAndGetTransactionAmount(performCurrencyExchangeRequest.getAmount());

        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        if (!isCurrencyAccountAlreadyExists(account, fromCurrency)) {
            throw new CurrencyAccountNotFoundException(fromCurrency.getCode());
        }

        if (!isCurrencyAccountAlreadyExists(account, toCurrency)) {
            throw new CurrencyAccountNotFoundException(toCurrency.getCode());
        }

        Optional<CurrencyAccount> fromCurrencyAccount = account.get().getCurrencyAccounts().stream()
                .filter(currencyAccount -> currencyAccount.getCurrency().getCode().equals(fromCurrency.getCode())).findFirst();

        Optional<CurrencyAccount> toCurrencyAccount = account.get().getCurrencyAccounts().stream()
                .filter(currencyAccount -> currencyAccount.getCurrency().getCode().equals(toCurrency.getCode())).findFirst();

        if (amount > fromCurrencyAccount.get().getBalance()) {
            throw new NotEnoughBalanceException(amount);
        }

        fromCurrencyAccount.get().setBalance(fromCurrencyAccount.get().getBalance() - amount);

        Double addedAmount = (fromCurrency.getParity() * amount) / toCurrency.getParity();

        toCurrencyAccount.get().setBalance(toCurrencyAccount.get().getBalance() + addedAmount);

        currencyAccountRepository.saveAndFlush(toCurrencyAccount.get());
        currencyAccountRepository.saveAndFlush(fromCurrencyAccount.get());

        return MapperUtils.mapToPerformCurrencyExchangeResponse(addedAmount, toCurrencyAccount.get());
    }

    void simulateExternalCall() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response
                    = restTemplate.getForEntity(ExternalUrls.HTTP_STATUS_URL, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Successful call");
            } else {
                logger.error("Unsuccessful call");
            }

        } catch (Exception e) {
            logger.error("Rest call failed");
        }
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
    @Transactional
    public PerformTransactionResponse performAddMoney(Long accountId, PerformTransactionRequest performTransactionRequest) {

        Double transactionAmount = validateAndGetTransactionAmount(performTransactionRequest.getAmount());

        Account account = validateAndGetAccount(accountId);

        validateCurrency(performTransactionRequest.getCurrencyCode());

        CurrencyAccount currencyAccount =
                validateAndGetCurrencyAccount(account, performTransactionRequest.getCurrencyCode());

        currencyAccount.setBalance(currencyAccount.getBalance() + transactionAmount);

        currencyAccountRepository.saveAndFlush(currencyAccount);

        return MapperUtils.mapToPerformTransactionResponse(currencyAccount);

    }

    private Currency validateCurrency(String currencyCode) {
        Currency currency = currencyService.getCurrency(currencyCode);

        if (currency == null) {
            throw new InvalidCurrencyException(currencyCode);
        }

        return currency;
    }

    private Account validateAndGetAccount(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }
        return account.get();
    }

    private Double validateAndGetTransactionAmount(Double amount) {
        if (amount < 1.0) {
            throw new MinimumTransactionAmountException(amount);
        }
        return amount;
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
