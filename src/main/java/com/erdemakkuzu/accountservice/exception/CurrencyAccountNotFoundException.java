package com.erdemakkuzu.accountservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyAccountNotFoundException extends RuntimeException{
    private final String currencyCode;
}
