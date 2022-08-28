package com.erdemakkuzu.accountservice.controller;

import com.erdemakkuzu.accountservice.constant.Operations;
import com.erdemakkuzu.accountservice.model.*;
import com.erdemakkuzu.accountservice.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = Operations.CREATE_ACCOUNT)
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody final CreateAccountRequest createAccountRequest) {
        return new ResponseEntity<>(accountService.createAccount(createAccountRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{accountId}/currency-account", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = Operations.CREATE_CURRENCY_ACCOUNT)
    public ResponseEntity<CreateCurrencyAccountResponse> createCurrencyAccount(@PathVariable("accountId") final Long accountId,
                                                                               @RequestBody CreateCurrencyAccountRequest createCurrencyAccountRequest) {
        return new ResponseEntity<>(accountService.createCurrencyAccount(accountId, createCurrencyAccountRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{accountId}/add-money", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = Operations.ADD_MONEY)
    public ResponseEntity<PerformTransactionResponse> performAddMoney(@PathVariable("accountId") final Long accountId,
                                                                      @RequestBody PerformTransactionRequest performTransactionRequest) {
        return new ResponseEntity<>(accountService.performAddMoney(accountId, performTransactionRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/{accountId}/debit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = Operations.DEBIT)
    public ResponseEntity<PerformTransactionResponse> performDebit(@PathVariable("accountId") final Long accountId,
                                                                   @RequestBody PerformTransactionRequest performTransactionRequest) {
        return new ResponseEntity<>(accountService.performDebit(accountId, performTransactionRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = Operations.GET_ACCOUNT)
    public ResponseEntity<GetAccountResponse> getAccount(@PathVariable("accountId") final Long accountId) {
        return new ResponseEntity<>(accountService.getAccount(accountId), HttpStatus.OK);
    }

    @PostMapping(value = "/{accountId}/exchange-currency", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = Operations.EXCHANGE_CURRENCY)
    public ResponseEntity<PerformCurrencyExchangeResponse> performExchangeCurrency(@PathVariable("accountId") final Long accountId,
                                                                                   @RequestBody PerformCurrencyExchangeRequest performCurrencyExchangeRequest) {
        return new ResponseEntity<>(accountService.performCurrencyExchange(accountId, performCurrencyExchangeRequest), HttpStatus.OK);
    }
}
