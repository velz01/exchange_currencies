package org.curr.exchangecurrencies.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dto.CreateExchangeDto;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.dto.ExchangeRatesDto;
import org.curr.exchangecurrencies.entity.ExchangeRates;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRatesMapper {
    private static final ExchangeRatesMapper INSTANCE = new ExchangeRatesMapper();

    public ExchangeRatesDto mapFrom(ExchangeRates exchangeRates, CurrencyDto baseCurrency, CurrencyDto targetCurrency) {
        return ExchangeRatesDto.builder()
                .id(exchangeRates.getId())
                .rate(exchangeRates.getRate())
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .build();

    }
    public ExchangeRates mapFrom(CreateExchangeDto createDto, Integer baseId, Integer targetId) {
        return ExchangeRates.builder()
                .baseCurrencyId(baseId)
                .targetCurrencyId(targetId)
                .rate(createDto.getRate())
                .build();
    }


    public static ExchangeRatesMapper getInstance() {
        return INSTANCE;
    }
}
