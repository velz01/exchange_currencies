package org.curr.exchangecurrencies.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ForExchangeDto {
    String baseCurrency;
    String targetCurrency;
    Integer amount;
}
