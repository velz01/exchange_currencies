package org.curr.exchangecurrencies.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class CreateCurrencyDto {
    String code;
    String fullName;
    String sign;
}
