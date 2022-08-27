package com.erdemakkuzu.accountservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateCurrencyAccountResponse {
    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("currency_balances")
    List<CurrencyBalance> currencyBalanceList;
}
