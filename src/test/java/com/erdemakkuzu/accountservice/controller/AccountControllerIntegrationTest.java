package com.erdemakkuzu.accountservice.controller;

import com.erdemakkuzu.accountservice.model.*;
import com.erdemakkuzu.accountservice.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    ObjectMapper mapper;

    ObjectWriter ow;

    private final Long DUMMY_ACCOUNT_ID = 1L;
    private final String DUMMY_CUSTOMER_FULL_NAME = "Erdem Akkuzu";
    private final String DUMMY_CURRENCY_CODE = "SEK";
    private final Double INITIAL_BALANCE = 0.0;
    private final Double TRANSACTION_AMOUNT = 15.0;
    private final String ACCOUNT_ENDPOINT = "/api/account";
    private final String ADD_MONEY_ENDPOINT_SUFFIX = "add-money";

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer();
        mapper.writer();
    }

    @SneakyThrows
    @Test
    void getAccount_happyFlow() {
        String resultString = "{\"account_id\":1,\"owner\":\"" + DUMMY_CUSTOMER_FULL_NAME + "\"}";

        GetAccountResponse getAccountResponse =
                new GetAccountResponse(DUMMY_ACCOUNT_ID, null, DUMMY_CUSTOMER_FULL_NAME, null);

        when(accountService.getAccount(1L)).thenReturn(getAccountResponse);

        mvc.perform(get(ACCOUNT_ENDPOINT + "/" + DUMMY_ACCOUNT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(resultString));
    }

    @SneakyThrows
    @Test
    void createAccount_happyFlow() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(DUMMY_CUSTOMER_FULL_NAME, DUMMY_CURRENCY_CODE);

        List<CurrencyBalance> currencyBalanceList = new ArrayList<>();
        currencyBalanceList.add(new CurrencyBalance(DUMMY_CURRENCY_CODE, INITIAL_BALANCE));

        CreateAccountResponse createAccountResponse = new CreateAccountResponse(DUMMY_ACCOUNT_ID, currencyBalanceList);

        String requestJson = ow.writeValueAsString(createAccountRequest);

        String expectedResponse = ow.writeValueAsString(createAccountResponse);

        when(accountService.createAccount(createAccountRequest)).thenReturn(createAccountResponse);

        mvc.perform(post(ACCOUNT_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedResponse));
    }

    @SneakyThrows
    @Test
    void addMoney_happyFlow() {
        PerformTransactionRequest performTransactionRequest = new PerformTransactionRequest(DUMMY_CURRENCY_CODE, TRANSACTION_AMOUNT);

        List<CurrencyBalance> currencyBalanceList = new ArrayList<>();
        currencyBalanceList.add(new CurrencyBalance(DUMMY_CURRENCY_CODE, TRANSACTION_AMOUNT));

        PerformTransactionResponse performTransactionResponse = new PerformTransactionResponse(DUMMY_ACCOUNT_ID, currencyBalanceList);

        String requestJson = ow.writeValueAsString(performTransactionRequest);

        String expectedResponse = ow.writeValueAsString(performTransactionResponse);

        when(accountService.performAddMoney(DUMMY_ACCOUNT_ID, performTransactionRequest)).thenReturn(performTransactionResponse);

        mvc.perform(post(ACCOUNT_ENDPOINT + "/" + DUMMY_ACCOUNT_ID + "/" + ADD_MONEY_ENDPOINT_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}