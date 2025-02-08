package org.curr.exchangecurrencies.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.entity.ExchangeRates;
import org.curr.exchangecurrencies.exception.CodeAlreadyExists;
import org.curr.exchangecurrencies.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRatesDao implements Dao<ExchangeRates> {
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();
    private static final String FIND_ALL = "SELECT * FROM exchange_rates";
    private static final String FIND_BY_IDS= "SELECT * FROM exchange_rates WHERE base_currency_id = ? AND target_currency_id = ?";
    @Override
    public List<ExchangeRates> findAll() throws SQLException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<ExchangeRates> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(buildExchangeCurrency(resultSet));
            }

            return list;
        }
    }

    public Optional<ExchangeRates> findById(Integer baseId, Integer targetId) throws SQLException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_IDS)) {
            preparedStatement.setObject(1, baseId);
            preparedStatement.setObject(2, targetId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRates exchangeRates = null;
            if (resultSet.next()) {
                exchangeRates = buildExchangeCurrency(resultSet);
            }
            return Optional.ofNullable(exchangeRates);
        }
    }

    public static ExchangeRatesDao getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRates save(ExchangeRates entity) throws SQLException, CodeAlreadyExists {
        return null;
    }

    @Override
    public void update(ExchangeRates entity) {

    }

    private static ExchangeRates buildExchangeCurrency(ResultSet resultSet) throws SQLException {
        return ExchangeRates.builder()
                .id(resultSet.getObject("id", Integer.class))
                .baseCurrencyId(resultSet.getObject("base_currency_id", Integer.class))
                .targetCurrencyId(resultSet.getObject("target_currency_id", Integer.class))
                .rate(resultSet.getObject("rate", Float.class))
                .build();
    }
}
