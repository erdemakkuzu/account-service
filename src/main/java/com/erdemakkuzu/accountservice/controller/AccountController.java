package com.erdemakkuzu.accountservice.controller;

import com.erdemakkuzu.accountservice.service.AccountService;
import com.erdemakkuzu.accountservice.model.CreateAccountRequest;
import com.erdemakkuzu.accountservice.model.CreateAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateAccountResponse> createPlayer(@RequestBody final CreateAccountRequest createAccountRequest) {
        return new ResponseEntity<>(accountService.createAccount(createAccountRequest), HttpStatus.CREATED);
    }
}
