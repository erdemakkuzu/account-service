package com.erdemakkuzu.accountservice.model;

public enum Currency {
    EURO("EUR"),
    US_DOLLAR("USD"),
    SWEDISH_KRONA("SEK"),
    RUSSIAN_RUBLE("RUB");

    private final String currencyCode;

    Currency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
