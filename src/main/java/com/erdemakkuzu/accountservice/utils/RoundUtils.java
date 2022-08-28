package com.erdemakkuzu.accountservice.utils;

public class RoundUtils {
    // rounds digits of the double after 2 digits from 0
    public static double round(double value) {
        double scale = Math.pow(10, 2);
        return Math.round(value * scale) / scale;
    }
}
