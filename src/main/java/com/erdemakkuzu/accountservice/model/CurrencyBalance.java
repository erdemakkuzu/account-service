package com.erdemakkuzu.accountservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyBalance {

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("balance")
    private Double balance;
}
