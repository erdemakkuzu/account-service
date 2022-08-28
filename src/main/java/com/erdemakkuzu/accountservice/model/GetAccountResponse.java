package com.erdemakkuzu.accountservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAccountResponse {
    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("currency_balances")
    List<CurrencyBalance> currencyBalanceList;
}
