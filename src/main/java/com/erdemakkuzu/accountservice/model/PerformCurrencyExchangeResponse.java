package com.erdemakkuzu.accountservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformCurrencyExchangeResponse {

    @JsonProperty("added_amount")
    private Double addedAmount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("new_balance")
    private Double newBalance;

}
