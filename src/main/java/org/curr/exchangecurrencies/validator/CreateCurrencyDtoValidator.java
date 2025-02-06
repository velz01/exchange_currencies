package org.curr.exchangecurrencies.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dto.CreateCurrencyDto;
import org.curr.exchangecurrencies.dto.CurrencyDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCurrencyDtoValidator implements Validator<CreateCurrencyDto> {
    private static final CreateCurrencyDtoValidator INSTANCE = new CreateCurrencyDtoValidator();

    @Override
    public boolean isValid(CreateCurrencyDto dto) {
        if (dto.getFullName() != null && dto.getCode() != null && dto.getSign() != null) {
            return true;
        }
        return false;
    }

    public static CreateCurrencyDtoValidator getInstance() {
        return INSTANCE;
    }
}
