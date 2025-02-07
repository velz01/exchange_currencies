package org.curr.exchangecurrencies.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dao.ExchangeCurrencyDao;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.dto.ExchangeCurrencyDto;
import org.curr.exchangecurrencies.entity.Currency;
import org.curr.exchangecurrencies.entity.ExchangeCurrency;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.mapper.CurrencyMapper;
import org.curr.exchangecurrencies.mapper.ExchangeCurrencyMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeCurrencyService {
    private static final ExchangeCurrencyService INSTANCE = new ExchangeCurrencyService();

    private final ExchangeCurrencyMapper mapper = ExchangeCurrencyMapper.getInstance();
    private final ExchangeCurrencyDao exchangeCurrencyDao = ExchangeCurrencyDao.getInstance();
    private final CurrencyService currencyService = CurrencyService.getInstance();

    public List<ExchangeCurrencyDto> findAll() throws SQLException, CurrencyNotFoundException {
        List<ExchangeCurrency> exchangeCurrencyList = exchangeCurrencyDao.findAll();
        List<ExchangeCurrencyDto> exchangeCurrencyDtoList = new ArrayList<>();
        for (ExchangeCurrency exchangeCurrency: exchangeCurrencyList) {
            Integer baseCurrencyId = exchangeCurrency.getBaseCurrencyId();
            Integer targetCurrencyId = exchangeCurrency.getTargetCurrencyId();
            CurrencyDto baseCurrencyDto = currencyService.findById(baseCurrencyId);
            CurrencyDto targetCurrencyDto = currencyService.findById(targetCurrencyId);
            ExchangeCurrencyDto exchangeCurrencyDto = mapper.mapFrom(exchangeCurrency, baseCurrencyDto, targetCurrencyDto);
            exchangeCurrencyDtoList.add(exchangeCurrencyDto);
        }
        System.out.println(exchangeCurrencyDtoList);
        return exchangeCurrencyDtoList;
    }
    public static ExchangeCurrencyService getInstance() {
        return INSTANCE;
    }
}
