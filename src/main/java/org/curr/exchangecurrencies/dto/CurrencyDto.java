package org.curr.exchangecurrencies.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrencyDto {
    Integer id;
    String code;
    String fullName;
    String sign;

}
