package org.curr.exchangecurrencies.dto;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class ExchangeCurrencyDto {
    Integer id;
    CurrencyDto baseCurrency;
    CurrencyDto targetCurrency;
    Float rate;
}
