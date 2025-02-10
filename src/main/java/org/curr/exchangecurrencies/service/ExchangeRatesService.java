package org.curr.exchangecurrencies.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.curr.exchangecurrencies.dao.CurrencyDao;
import org.curr.exchangecurrencies.dao.ExchangeRatesDao;
import org.curr.exchangecurrencies.dto.*;
import org.curr.exchangecurrencies.entity.Currency;
import org.curr.exchangecurrencies.entity.ExchangeRates;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.exception.ExchangeRatesAlreadyExists;
import org.curr.exchangecurrencies.exception.ExchangeRatesNotFound;
import org.curr.exchangecurrencies.mapper.ExchangeDtoMapper;
import org.curr.exchangecurrencies.mapper.ExchangeRatesMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRatesService {
    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();
    private static final String FOR_CROSS_COURSE = "USD";
    private final ExchangeRatesMapper ratesMapper = ExchangeRatesMapper.getInstance();
    private final ExchangeDtoMapper exchangeDtoMapper = ExchangeDtoMapper.getInstance();
    private final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final CurrencyService currencyService = CurrencyService.getInstance();

    public List<ExchangeRatesDto> findAll() throws SQLException, CurrencyNotFoundException {
        return exchangeRatesDao.findAll().stream().map(this::convertToDto).collect(Collectors.toList());

    }
    @SneakyThrows
    private ExchangeRatesDto convertToDto(ExchangeRates exchangeRates) {

        CurrencyDto baseCurrency = currencyService.findById(exchangeRates.getBaseCurrencyId());
        CurrencyDto targetCurrency = currencyService.findById(exchangeRates.getTargetCurrencyId());
        return ratesMapper.mapFrom(exchangeRates, baseCurrency, targetCurrency);
    }

    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesDto findByCodes(String uri) throws SQLException, CurrencyNotFoundException, ExchangeRatesNotFound {
        String baseCode = uri.substring(0, 3);
        String targetCode = uri.substring(3, 6);
        CurrencyDto baseCurrencyDto = currencyService.findByCode(baseCode);//
        CurrencyDto targetCurrencyDto = currencyService.findByCode(targetCode);
        Optional<ExchangeRates> optionalExchangeRates = exchangeRatesDao.findById(baseCurrencyDto.getId(), targetCurrencyDto.getId());
        if (optionalExchangeRates.isPresent()) {
            ExchangeRates exchangeRates = optionalExchangeRates.get();
            return ratesMapper.mapFrom(exchangeRates, baseCurrencyDto, targetCurrencyDto);
        }
        throw new ExchangeRatesNotFound();


    }

    private Float getRoundValue(Float value) {
        BigDecimal bigDecimal = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.floatValue();
    }

    public ExchangeDto obtainExchange(ForExchangeDto forExchangeDto) throws SQLException, CurrencyNotFoundException, ExchangeRatesNotFound {
        CurrencyDto base = currencyService.findByCode(forExchangeDto.getBaseCurrency());
        CurrencyDto target = currencyService.findByCode(forExchangeDto.getTargetCurrency());

        Integer amount = forExchangeDto.getAmount();

        Optional<ExchangeDto> directExchangeDto = tryGetDirectRate(base, target, amount);
        if (directExchangeDto.isPresent()) {
            return directExchangeDto.get();
        }
        Optional<ExchangeDto> reverseExchangeDto = tryGetReverseRate(base, target, amount);
        if (reverseExchangeDto.isPresent()) {
            return reverseExchangeDto.get();
        }
        Optional<ExchangeDto> crossExchangeDto = tryGetCrossRate(base, target, amount);
        if (crossExchangeDto.isPresent()) {
            return crossExchangeDto.get();
        }
        throw new ExchangeRatesNotFound();

    }

    private Optional<ExchangeDto> tryGetDirectRate(CurrencyDto base, CurrencyDto target, Integer amount) throws SQLException {
        return exchangeRatesDao.findById(base.getId(), target.getId())
                .map(exchangeRates -> exchangeDtoMapper.map(
                        base,
                        target,
                        exchangeRates.getRate(),
                        amount,
                        getRoundValue(exchangeRates.getRate() * amount)));


    }

    private Optional<ExchangeDto> tryGetReverseRate(CurrencyDto base, CurrencyDto target, Integer amount) throws SQLException {
        return exchangeRatesDao.findById(target.getId(), base.getId())
                .map(exchangeRates -> exchangeDtoMapper.map(
                        base,
                        target,
                        getRoundValue(1 / exchangeRates.getRate()),
                        amount,
                        getRoundValue( (1 / exchangeRates.getRate()) * amount)));


    }



    private Optional<ExchangeDto> tryGetCrossRate(CurrencyDto base, CurrencyDto target, Integer amount) throws SQLException {

        Optional<Currency> currencyOptional = currencyDao.findByCode(FOR_CROSS_COURSE);
        if (currencyOptional.isEmpty()) return Optional.empty();
        Integer usdCurrencyId = currencyOptional.get().getId();
        Optional<ExchangeRates> baseExchangeRatesOptional = exchangeRatesDao.findById(base.getId(), usdCurrencyId);
        Optional<ExchangeRates> targetExchangeRatesOptional = exchangeRatesDao.findById(target.getId(), usdCurrencyId);
        if (baseExchangeRatesOptional.isPresent() && targetExchangeRatesOptional.isPresent()) {
            Float rate = getRoundValue( baseExchangeRatesOptional.get().getRate() / targetExchangeRatesOptional.get().getRate());
            return Optional.of(exchangeDtoMapper.map(base, target, rate, amount, rate * amount));
        }
        return Optional.empty();

    }

    public ExchangeRatesDto save(CreateExchangeDto dto) throws SQLException, CurrencyNotFoundException, ExchangeRatesAlreadyExists {
        CurrencyDto baseCurrencyDto = currencyService.findByCode(dto.getBaseCurrency());
        CurrencyDto targetCurrencyDto = currencyService.findByCode(dto.getTargetCurrency());

        ExchangeRates exchangeRates = ratesMapper.mapFrom(dto, baseCurrencyDto.getId(), targetCurrencyDto.getId());
        ExchangeRates exchangeRatesWithId = exchangeRatesDao.save(exchangeRates);
        return ratesMapper.mapFrom(exchangeRatesWithId, baseCurrencyDto, targetCurrencyDto);
    }

    public ExchangeRatesDto update(CreateExchangeDto dto) throws SQLException, CurrencyNotFoundException, ExchangeRatesNotFound {
        CurrencyDto baseCurrencyDto = currencyService.findByCode(dto.getBaseCurrency());
        CurrencyDto targetCurrencyDto = currencyService.findByCode(dto.getTargetCurrency());

        ExchangeRates updatedExchangeRates = ratesMapper.mapFrom(dto, baseCurrencyDto.getId(), targetCurrencyDto.getId());
        Optional<ExchangeRates> exchangeRates = exchangeRatesDao.findById(baseCurrencyDto.getId(), targetCurrencyDto.getId());
        if (exchangeRates.isPresent()) {
            updatedExchangeRates.setId(exchangeRates.get().getId());
            exchangeRatesDao.update(updatedExchangeRates);
            return ratesMapper.mapFrom(updatedExchangeRates, baseCurrencyDto, targetCurrencyDto);
        }
        throw new ExchangeRatesNotFound();

    }
}
