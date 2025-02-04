package org.curr.exchangecurrencies.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {
    private Integer id;
    private String code;
    private String fullName;
    private String sign;
}
