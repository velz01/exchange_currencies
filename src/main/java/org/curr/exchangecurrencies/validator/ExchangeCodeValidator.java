package org.curr.exchangecurrencies.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dao.ExchangeRatesDao;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeCodeValidator implements Validator<String>{
    private static final ExchangeCodeValidator INSTANCE = new ExchangeCodeValidator();
    @Override
    public boolean isValid(String codes) {
        return codes.length() == 6 && codes.toUpperCase().equals(codes) && !(containsDigit(codes));
    }
    private boolean containsDigit(String codes) {
        for (char c : codes.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    public static ExchangeCodeValidator getInstance() {
        return INSTANCE;
    }
}
