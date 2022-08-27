package com.erdemakkuzu.accountservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountNotFoundException extends RuntimeException {
    private Long accountId;
}
