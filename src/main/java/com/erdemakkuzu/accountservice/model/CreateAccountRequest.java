package com.erdemakkuzu.accountservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    @JsonProperty("customer_full_name")
    private String customerFullName;

    @JsonProperty("initial_currency")
    private String initialCurrency;
}
