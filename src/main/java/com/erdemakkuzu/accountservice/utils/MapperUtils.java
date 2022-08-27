package com.erdemakkuzu.accountservice.utils;

import com.erdemakkuzu.accountservice.entity.CurrencyAccount;
import com.erdemakkuzu.accountservice.model.CreateAccountResponse;
import com.erdemakkuzu.accountservice.model.CurrencyBalance;

import java.util.Collections;

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
}
