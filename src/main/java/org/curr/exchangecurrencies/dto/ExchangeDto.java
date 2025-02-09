package org.curr.exchangecurrencies.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExchangeDto {
    CurrencyDto baseCurrencyDto;
    CurrencyDto targetCurrencyDto;
    Float rate;
    Integer amount;
    Float convertedAmount;
}
