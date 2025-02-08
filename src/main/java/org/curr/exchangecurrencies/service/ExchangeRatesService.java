package org.curr.exchangecurrencies.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dao.ExchangeRatesDao;
import org.curr.exchangecurrencies.dto.CreateExchangeDto;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.dto.ExchangeRatesDto;
import org.curr.exchangecurrencies.entity.ExchangeRates;
import org.curr.exchangecurrencies.exception.CodeAlreadyExists;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.exception.ExchangeRatesAlreadyExists;
import org.curr.exchangecurrencies.exception.ExchangeRatesNotFound;
import org.curr.exchangecurrencies.mapper.ExchangeRatesMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRatesService {
    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private final ExchangeRatesMapper mapper = ExchangeRatesMapper.getInstance();
    private final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();
    private final CurrencyService currencyService = CurrencyService.getInstance();

    public List<ExchangeRatesDto> findAll() throws SQLException, CurrencyNotFoundException {
        List<ExchangeRates> exchangeRatesList = exchangeRatesDao.findAll();
        List<ExchangeRatesDto> exchangeRatesDtoList = new ArrayList<>();
        for (ExchangeRates exchangeRates : exchangeRatesList) {
            Integer baseCurrencyId = exchangeRates.getBaseCurrencyId();
            Integer targetCurrencyId = exchangeRates.getTargetCurrencyId();
            CurrencyDto baseCurrencyDto = currencyService.findById(baseCurrencyId);
            CurrencyDto targetCurrencyDto = currencyService.findById(targetCurrencyId);
            ExchangeRatesDto exchangeRatesDto = mapper.mapFrom(exchangeRates, baseCurrencyDto, targetCurrencyDto);
            exchangeRatesDtoList.add(exchangeRatesDto);
        }

        return exchangeRatesDtoList;
    }
    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesDto findByCodes(String uri) throws SQLException, CurrencyNotFoundException, ExchangeRatesNotFound {
        String baseCode = uri.substring(0,3);
        String targetCode = uri.substring(3,6);
        CurrencyDto baseCurrencyDto = currencyService.findByCode(baseCode);//
        CurrencyDto targetCurrencyDto = currencyService.findByCode(targetCode);
        Optional<ExchangeRates> optionalExchangeRates = exchangeRatesDao.findById(baseCurrencyDto.getId(), targetCurrencyDto.getId());
        if (optionalExchangeRates.isPresent()) {
            ExchangeRates exchangeRates = optionalExchangeRates.get();
            return mapper.mapFrom(exchangeRates, baseCurrencyDto, targetCurrencyDto);
        }
        throw new ExchangeRatesNotFound();


    }

    public ExchangeRatesDto save(CreateExchangeDto dto) throws SQLException, CurrencyNotFoundException, ExchangeRatesAlreadyExists {
        CurrencyDto baseCurrencyDto = currencyService.findByCode(dto.getBaseCurrency());
        CurrencyDto targetCurrencyDto = currencyService.findByCode(dto.getTargetCurrency());

        ExchangeRates exchangeRates = mapper.mapFrom(dto, baseCurrencyDto.getId(), targetCurrencyDto.getId());
        ExchangeRates exchangeRatesWithId = exchangeRatesDao.save(exchangeRates);
        return mapper.mapFrom(exchangeRatesWithId, baseCurrencyDto, targetCurrencyDto);
    }

    public ExchangeRatesDto update(CreateExchangeDto dto) throws SQLException, CurrencyNotFoundException, ExchangeRatesNotFound {
        CurrencyDto baseCurrencyDto = currencyService.findByCode(dto.getBaseCurrency());
        CurrencyDto targetCurrencyDto = currencyService.findByCode(dto.getTargetCurrency());

        ExchangeRates updatedExchangeRates = mapper.mapFrom(dto, baseCurrencyDto.getId(), targetCurrencyDto.getId());
        Optional<ExchangeRates> exchangeRates = exchangeRatesDao.findById(baseCurrencyDto.getId(), targetCurrencyDto.getId());
        if (exchangeRates.isPresent()) {
            updatedExchangeRates.setId(exchangeRates.get().getId());
            exchangeRatesDao.update(updatedExchangeRates);
            return mapper.mapFrom(updatedExchangeRates, baseCurrencyDto, targetCurrencyDto);
        }
        throw new ExchangeRatesNotFound();

    }
}
