package com.erdemakkuzu.accountservice.exception;

import com.erdemakkuzu.accountservice.constant.FieldKeys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<?> handleInvalidCurrencyException(InvalidCurrencyException invalidCurrencyException) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(ErrorMessageKeys.INVALID_CURRENCY);
        errorDetails.setValue(invalidCurrencyException.getInvalidCurrency());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(ErrorMessageKeys.ACCOUNT_NOT_FOUND);
        errorDetails.setValue(accountNotFoundException.getAccountId().toString());
        errorDetails.setField(FieldKeys.ACCOUNT_ID);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CurrencyAccountAlreadyExistsException.class)
    public ResponseEntity<?> handleCurrencyAccountAlreadyExistsException(CurrencyAccountAlreadyExistsException currencyAccountAlreadyExistsException) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(ErrorMessageKeys.CURRENCY_ACCOUNT_ALREADY_EXISTS);
        errorDetails.setValue(currencyAccountAlreadyExistsException.getCurrencyCode());
        errorDetails.setField(FieldKeys.CURRENCY_CODE);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyAccountNotFoundException.class)
    public ResponseEntity<?> handleCurrencyAccountNotFoundException(CurrencyAccountNotFoundException currencyAccountNotFoundException) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(ErrorMessageKeys.CURRENCY_ACCOUNT_NOT_FOUND);
        errorDetails.setValue(currencyAccountNotFoundException.getCurrencyCode());
        errorDetails.setField(FieldKeys.CURRENCY_CODE);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MinimumTransactionAmountException.class)
    public ResponseEntity<?> handleMinimumTransactionAmountExceptionException(MinimumTransactionAmountException minimumTransactionAmountException) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(ErrorMessageKeys.INVALID_TRANSACTION_AMOUNT);
        errorDetails.setValue(minimumTransactionAmountException.getAmount().toString());
        errorDetails.setField(FieldKeys.AMOUNT);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<?> handleNotEnoughBalanceException(NotEnoughBalanceException notEnoughBalanceException) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(ErrorMessageKeys.NOT_ENOUGH_BALANCE);
        errorDetails.setValue(notEnoughBalanceException.getDebitAmount().toString());
        errorDetails.setField(FieldKeys.AMOUNT);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
