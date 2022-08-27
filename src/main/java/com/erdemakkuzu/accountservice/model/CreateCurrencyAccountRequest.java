package com.erdemakkuzu.accountservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCurrencyAccountRequest {
    @JsonProperty("currency_code")
    private String currencyCode;
}
