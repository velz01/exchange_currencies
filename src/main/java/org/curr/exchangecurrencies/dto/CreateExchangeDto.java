package org.curr.exchangecurrencies.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateExchangeDto {
    String baseCurrency;
    String targetCurrency;
    Float rate;
}
