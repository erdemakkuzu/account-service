package com.erdemakkuzu.accountservice.service.impl;

import com.erdemakkuzu.accountservice.entity.Account;
import com.erdemakkuzu.accountservice.entity.Currency;
import com.erdemakkuzu.accountservice.exception.AccountNotFoundException;
import com.erdemakkuzu.accountservice.exception.InvalidCurrencyException;
import com.erdemakkuzu.accountservice.model.CreateAccountRequest;
import com.erdemakkuzu.accountservice.model.CreateAccountResponse;
import com.erdemakkuzu.accountservice.model.GetAccountResponse;
import com.erdemakkuzu.accountservice.repository.AccountRepository;
import com.erdemakkuzu.accountservice.repository.CurrencyAccountRepository;
import com.erdemakkuzu.accountservice.service.CurrencyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private static String DUMMY_CUSTOMER_FULL_NAME = "Erdem AKKUZU";
    private static String INITIAL_VALID_CURRENCY = "SEK";
    private static String INITIAL_INVALID_CURRENCY = "KES";
    private static Long DUMMY_ACCOUNT_ID = 1L;

    private CurrencyService currencyService;

    private AccountServiceImpl accountServiceImpl;

    private AccountRepository accountRepository;

    private CurrencyAccountRepository currencyAccountRepository;


    @BeforeEach
    public void setup() {
        currencyService = mock(CurrencyService.class);
        accountRepository = mock(AccountRepository.class);
        currencyAccountRepository = mock(CurrencyAccountRepository.class);
        accountServiceImpl = new AccountServiceImpl(accountRepository, currencyAccountRepository, currencyService);

    }

    @Test
    public void testCreateAccount_happyFlow() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setCustomerFullName(DUMMY_CUSTOMER_FULL_NAME);
        createAccountRequest.setInitialCurrency(INITIAL_VALID_CURRENCY);

        Currency currency = new Currency();
        currency.setParity(1.1);
        currency.setId(1L);
        currency.setCode(INITIAL_VALID_CURRENCY);

        when(currencyService.getCurrency(eq(INITIAL_VALID_CURRENCY))).thenReturn(currency);
        when(accountRepository.saveAndFlush(any(Account.class)))
                .thenReturn(new Account(DUMMY_ACCOUNT_ID, DUMMY_CUSTOMER_FULL_NAME, new Date(), null));

        CreateAccountResponse createAccountResponse = accountServiceImpl.createAccount(createAccountRequest);

        verify(accountRepository, times(1)).saveAndFlush(any());
        Assertions.assertEquals(DUMMY_ACCOUNT_ID, createAccountResponse.getAccountId());
    }

    @Test
    public void testCreateAccount_invalidCurrency_Exception() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setCustomerFullName(DUMMY_CUSTOMER_FULL_NAME);
        createAccountRequest.setInitialCurrency(INITIAL_VALID_CURRENCY);

        when(currencyService.getCurrency(eq(INITIAL_VALID_CURRENCY))).thenReturn(null);
        Assertions.assertThrows(InvalidCurrencyException.class, () -> {
            accountServiceImpl.createAccount(createAccountRequest);
        });
    }

    @Test
    public void testGetAccount_happyFlow() {
        when(accountRepository.findById(DUMMY_ACCOUNT_ID))
                .thenReturn(Optional.of
                        (new Account(DUMMY_ACCOUNT_ID, DUMMY_CUSTOMER_FULL_NAME, new Date(), new ArrayList<>()))
                );

        GetAccountResponse getAccountResponse = accountServiceImpl.getAccount(DUMMY_ACCOUNT_ID);

        Assertions.assertEquals(DUMMY_ACCOUNT_ID, getAccountResponse.getAccountId());
        Assertions.assertEquals(0, getAccountResponse.getCurrencyBalanceList().size());
        Assertions.assertEquals(DUMMY_CUSTOMER_FULL_NAME, getAccountResponse.getOwner());
    }

    @Test
    public void testGetAccount_accountNotFound_Exception() {
        when(accountRepository.findById(DUMMY_ACCOUNT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountServiceImpl.getAccount(DUMMY_ACCOUNT_ID);
        });

    }
}