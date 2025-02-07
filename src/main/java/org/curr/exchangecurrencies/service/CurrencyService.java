package org.curr.exchangecurrencies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import lombok.SneakyThrows;
import org.curr.exchangecurrencies.dao.CurrencyDao;
import org.curr.exchangecurrencies.dto.CreateCurrencyDto;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.entity.Currency;
import org.curr.exchangecurrencies.exception.CodeAlreadyExists;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.mapper.CurrencyMapper;
import org.curr.exchangecurrencies.util.ErrorType;
import org.curr.exchangecurrencies.util.JsonUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();


    private final CurrencyMapper mapper = CurrencyMapper.getInstance();


    public List<CurrencyDto> findAll() throws SQLException {
        return currencyDao.findAll().stream().map(mapper::mapFrom).collect(Collectors.toList());

    }

//    @SneakyThrows
    public CurrencyDto create(CreateCurrencyDto dto) throws CodeAlreadyExists, SQLException {
        Currency currency = mapper.mapFrom(dto);
        Currency currencyWithId = currencyDao.save(currency);
        return mapper.mapFrom(currencyWithId);


    }


    public CurrencyDto findByCode(String code) throws CurrencyNotFoundException, SQLException {
        Optional<Currency> currencyOptional = currencyDao.findByCode(code);

        if (currencyOptional.isPresent()) {
            return mapper.mapFrom(currencyOptional.get());

        } else {
            throw new CurrencyNotFoundException();
        }
    }

    public CurrencyDto findById(Integer id) throws CurrencyNotFoundException, SQLException {
        Optional<Currency> currencyOptional = currencyDao.findById(id);

        if (currencyOptional.isPresent()) {
            return mapper.mapFrom(currencyOptional.get());

        } else {
            throw new CurrencyNotFoundException();
        }


    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
