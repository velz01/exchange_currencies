package org.curr.exchangecurrencies.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dto.CreateExchangeDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateExchangeRatesValidator implements Validator<CreateExchangeDto> {
    private static final CreateExchangeRatesValidator INSTANCE = new CreateExchangeRatesValidator();

    @Override
    public boolean isValid(CreateExchangeDto dto) {
        return !(dto.getBaseCurrency().isEmpty()) &&
                !(dto.getTargetCurrency().isEmpty()) &&
                dto.getRate() > 0;
    }

    public static CreateExchangeRatesValidator getInstance() {
        return INSTANCE;
    }
}
