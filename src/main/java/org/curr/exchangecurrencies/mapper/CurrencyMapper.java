package org.curr.exchangecurrencies.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dto.CreateCurrencyDto;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.entity.Currency;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyMapper {
    private static final CurrencyMapper INSTANCE = new CurrencyMapper();
    public Currency mapFrom(CreateCurrencyDto dto) {
        return Currency.builder()
                .code(dto.getCode())
                .fullName(dto.getFullName())
                .sign(dto.getSign())
                .build();

    }
    public CurrencyDto mapFrom(Currency currency) {
        return CurrencyDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .fullName(currency.getFullName())
                .sign(currency.getSign())
                .build();

    }
    public static CurrencyMapper getInstance() {
        return INSTANCE;
    }
}
