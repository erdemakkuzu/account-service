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

        List<CurrencyBalance> currencyBalanceList = getCurrencyAccountList(account);

        currencyBalanceList.add(new CurrencyBalance(createCurrencyAccountRequest.getCurrencyCode(), 0.0));

        createCurrencyAccountResponse.setCurrencyBalanceList(currencyBalanceList);

        return createCurrencyAccountResponse;
    }

    public static PerformTransactionResponse mapToPerformTransactionResponse(CurrencyAccount currencyAccount) {
        PerformTransactionResponse performTransactionResponse = new PerformTransactionResponse();
        performTransactionResponse.setAccountId(currencyAccount.getAccount().getId());

        Account account = currencyAccount.getAccount();
        List<CurrencyBalance> currencyBalanceList = getCurrencyAccountList(account);

        performTransactionResponse.setCurrencyBalanceList(currencyBalanceList);

        return performTransactionResponse;


    }

    public static GetAccountResponse mapToGetAccountResponse(Account account) {
        GetAccountResponse getAccountResponse = new GetAccountResponse();
        getAccountResponse.setAccountId(account.getId());
        getAccountResponse.setCreatedDate(account.getCreatedDate());
        getAccountResponse.setOwner(account.getCustomerFullName());

        List<CurrencyBalance> currencyBalanceList = getCurrencyAccountList(account);

        getAccountResponse.setCurrencyBalanceList(currencyBalanceList);

        return getAccountResponse;

    }

    public static List<CurrencyBalance> getCurrencyAccountList(Account account) {
        List<CurrencyBalance> currencyBalanceList = new ArrayList<>();

        for (CurrencyAccount currencyAccount : account.getCurrencyAccounts()) {
            String currencyCode = currencyAccount.getCurrency().getCode();
            Double balance = currencyAccount.getBalance();
            currencyBalanceList.add(new CurrencyBalance(currencyCode, balance));
        }

        return currencyBalanceList;
    }
}
