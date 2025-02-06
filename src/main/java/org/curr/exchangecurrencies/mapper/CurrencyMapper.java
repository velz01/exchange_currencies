package org.curr.exchangecurrencies.mapper;

import org.curr.exchangecurrencies.dto.CreateCurrencyDto;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.entity.Currency;

public class CurrencyMapper {

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
}
