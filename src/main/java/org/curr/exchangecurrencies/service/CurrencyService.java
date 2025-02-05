package org.curr.exchangecurrencies.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import lombok.SneakyThrows;
import org.curr.exchangecurrencies.dao.CurrencyDao;
import org.curr.exchangecurrencies.dto.CreateCurrencyDto;
import org.curr.exchangecurrencies.entity.Currency;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.mapper.CreateCurrencyMapper;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final CreateCurrencyMapper mapper = new CreateCurrencyMapper();
    @SneakyThrows
    public String findAll() {
        return jsonMapper.writeValueAsString(currencyDao.findAll());
    }

    @SneakyThrows
    public String create(CreateCurrencyDto dto) {
        Currency currency = mapper.mapFrom(dto);
        Currency currencyWithId = currencyDao.save(currency);
        return jsonMapper.writeValueAsString(currencyWithId);

    }


    public String findByCode(String code) throws JsonProcessingException, CurrencyNotFoundException {
        Optional<Currency> currencyOptional = currencyDao.findByCode(code);

        if (currencyOptional.isPresent()) {
            return jsonMapper.writeValueAsString(currencyOptional.get());
        } else {
            throw new CurrencyNotFoundException("Currency not found for code: " + code);
        }


    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
