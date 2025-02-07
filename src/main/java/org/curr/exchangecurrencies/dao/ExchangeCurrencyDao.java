package org.curr.exchangecurrencies.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.curr.exchangecurrencies.dto.ExchangeCurrencyDto;
import org.curr.exchangecurrencies.entity.ExchangeCurrency;
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
public class ExchangeCurrencyDao implements Dao<ExchangeCurrency> {
    private static final ExchangeCurrencyDao INSTANCE = new ExchangeCurrencyDao();
    private static final String FIND_ALL = "SELECT * FROM exchange_rates";

    @Override
    public List<ExchangeCurrency> findAll() throws SQLException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<ExchangeCurrency> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(buildExchangeCurrency(resultSet));
            }

            return list;
        }
    }

    public static ExchangeCurrencyDao getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeCurrency save(ExchangeCurrency entity) throws SQLException, CodeAlreadyExists {
        return null;
    }

    @Override
    public void update(ExchangeCurrency entity) {

    }

    private static ExchangeCurrency buildExchangeCurrency(ResultSet resultSet) throws SQLException {
        return ExchangeCurrency.builder()
                .id(resultSet.getObject("id", Integer.class))
                .baseCurrencyId(resultSet.getObject("base_currency_id", Integer.class))
                .targetCurrencyId(resultSet.getObject("target_currency_id", Integer.class))
                .rate(resultSet.getObject("rate", Float.class))
                .build();
    }
}
