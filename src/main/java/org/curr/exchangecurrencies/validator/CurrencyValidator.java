package org.curr.exchangecurrencies.validator;

import java.util.Arrays;

public class CurrencyValidator implements Validator<String> {
    private static final CurrencyValidator INSTANCE = new CurrencyValidator();
    @Override
    public boolean isValid(String code) {
        return (code.toUpperCase().equals(code) && !(containsDigit(code)) && !(code.isEmpty()));
    }

    private boolean containsDigit(String code) {
        for (char c : code.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    public static CurrencyValidator getInstance() {
        return INSTANCE;
    }
}
