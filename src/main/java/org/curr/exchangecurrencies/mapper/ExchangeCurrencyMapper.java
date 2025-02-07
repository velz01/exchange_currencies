package org.curr.exchangecurrencies.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.dto.ExchangeCurrencyDto;
import org.curr.exchangecurrencies.entity.Currency;
import org.curr.exchangecurrencies.entity.ExchangeCurrency;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeCurrencyMapper {
    private static final ExchangeCurrencyMapper INSTANCE = new ExchangeCurrencyMapper();

    public ExchangeCurrencyDto mapFrom(ExchangeCurrency exchangeCurrency, CurrencyDto baseCurrency, CurrencyDto targetCurrency) {
        return ExchangeCurrencyDto.builder()
                .id(exchangeCurrency.getId())
                .rate(exchangeCurrency.getRate())
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .build();

    }

    public static ExchangeCurrencyMapper getInstance() {
        return INSTANCE;
    }
}
