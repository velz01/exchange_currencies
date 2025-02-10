package org.curr.exchangecurrencies.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.dto.ExchangeDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeDtoMapper {
    private static final ExchangeDtoMapper INSTANCE = new ExchangeDtoMapper();

    public ExchangeDto map(CurrencyDto base, CurrencyDto target, Float rate, Integer amount, Float convertedAmount) {
        return ExchangeDto.builder()
                .baseCurrencyDto(base)
                .targetCurrencyDto(target)
                .rate(rate)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }

    public static ExchangeDtoMapper getInstance() {
        return INSTANCE;
    }
}
