package com.erdemakkuzu.accountservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyAccountAlreadyExistsException extends RuntimeException{
    private final String currencyCode;
}
