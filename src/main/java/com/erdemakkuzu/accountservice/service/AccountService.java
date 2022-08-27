package com.erdemakkuzu.accountservice.service;

import com.erdemakkuzu.accountservice.model.CreateAccountRequest;
import com.erdemakkuzu.accountservice.model.CreateAccountResponse;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);

}
