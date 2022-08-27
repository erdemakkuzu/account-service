package com.erdemakkuzu.accountservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidCurrencyException extends RuntimeException{
    private String invalidCurrency;
}
