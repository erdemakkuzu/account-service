package com.erdemakkuzu.accountservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotEnoughBalanceException extends RuntimeException{

    private final Double debitAmount;
}
