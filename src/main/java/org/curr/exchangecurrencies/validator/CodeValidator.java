package org.curr.exchangecurrencies.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeValidator implements Validator<String> {
    private static final CodeValidator INSTANCE = new CodeValidator();
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

    public static CodeValidator getInstance() {
        return INSTANCE;
    }
}
