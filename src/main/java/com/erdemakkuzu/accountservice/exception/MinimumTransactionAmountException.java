package com.erdemakkuzu.accountservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinimumTransactionAmountException extends RuntimeException{
    private final Double amount;
}
