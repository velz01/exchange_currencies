package org.curr.exchangecurrencies.mapper;

import org.curr.exchangecurrencies.dto.CreateCurrencyDto;
import org.curr.exchangecurrencies.entity.Currency;

public class CreateCurrencyMapper {

    public Currency mapFrom(CreateCurrencyDto dto) {
        return Currency.builder()
                .code(dto.getCode())
                .fullName(dto.getFullName())
                .sign(dto.getSign())
                .build();

    }
}
