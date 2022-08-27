package com.erdemakkuzu.accountservice.utils;

import com.erdemakkuzu.accountservice.entity.Account;
import com.erdemakkuzu.accountservice.entity.CurrencyAccount;
import com.erdemakkuzu.accountservice.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapperUtils {
    public static CreateAccountResponse mapToCreateAccountResponse(CurrencyAccount currencyAccount) {
        CreateAccountResponse createAccountResponse = new CreateAccountResponse();
        createAccountResponse.setAccountId(currencyAccount.getAccount().getId());
        createAccountResponse.setCurrencyBalanceList(
                Collections.singletonList(
                        new CurrencyBalance(currencyAccount.getCurrency().getCode(), currencyAccount.getBalance())
                )
        );

        return createAccountResponse;
    }

    public static CreateCurrencyAccountResponse mapToCreateCurrencyAccountResponse(CurrencyAccount currencyAccount,
                                                                                   CreateCurrencyAccountRequest createCurrencyAccountRequest) {
        Account account = currencyAccount.getAccount();
        CreateCurrencyAccountResponse createCurrencyAccountResponse = new CreateCurrencyAccountResponse();
        createCurrencyAccountResponse.setAccountId(account.getId());

        List<CurrencyBalance> currencyBalanceList = new ArrayList<>();

        for (CurrencyAccount currencyAccountOfAccount : account.getCurrencyAccounts()) {
            String currencyCode = currencyAccountOfAccount.getCurrency().getCode();
            Double balance = currencyAccountOfAccount.getBalance();
            currencyBalanceList.add(new CurrencyBalance(currencyCode, balance));
        }

        currencyBalanceList.add(new CurrencyBalance(createCurrencyAccountRequest.getCurrencyCode(), 0.0));

        createCurrencyAccountResponse.setCurrencyBalanceList(currencyBalanceList);

        return createCurrencyAccountResponse;
    }

    public static PerformTransactionResponse mapToPerformTransactionResponse(CurrencyAccount currencyAccount) {
        PerformTransactionResponse performTransactionResponse = new PerformTransactionResponse();
        performTransactionResponse.setAccountId(currencyAccount.getAccount().getId());

        Account account = currencyAccount.getAccount();
        List<CurrencyBalance> currencyBalanceList = new ArrayList<>();

        for (CurrencyAccount currencyAccountOfAccount : account.getCurrencyAccounts()) {
            String currencyCode = currencyAccountOfAccount.getCurrency().getCode();
            Double balance = currencyAccountOfAccount.getBalance();
            currencyBalanceList.add(new CurrencyBalance(currencyCode, balance));
        }

        performTransactionResponse.setCurrencyBalanceList(currencyBalanceList);

        return performTransactionResponse;


    }
}
