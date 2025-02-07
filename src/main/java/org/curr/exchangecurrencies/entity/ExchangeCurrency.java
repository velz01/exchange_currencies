package org.curr.exchangecurrencies.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeCurrency {
    Integer id;
    Integer baseCurrencyId;
    Integer targetCurrencyId;
    Float rate;
}
