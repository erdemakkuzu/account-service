package com.erdemakkuzu.accountservice.service;

import com.erdemakkuzu.accountservice.model.*;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);

    PerformTransactionResponse performAddMoney(Long accountId, PerformTransactionRequest performTransactionRequest);

    CreateCurrencyAccountResponse createCurrencyAccount(Long accountId, CreateCurrencyAccountRequest createCurrencyAccountRequest);
}
