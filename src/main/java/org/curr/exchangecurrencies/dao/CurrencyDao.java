package org.curr.exchangecurrencies.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.curr.exchangecurrencies.entity.Currency;
import org.curr.exchangecurrencies.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyDao implements Dao<Currency> {

    private static final String FIND_ALL = "SELECT * FROM currencies";
    private static final String FIND_BY_CODE = "SELECT * FROM currencies WHERE code = ?";
    private static final String CREATE_CURRENCY = "INSERT INTO currencies (code, full_name, sign) VALUES (?,?,?)";
    private static final CurrencyDao INSTANCE = new CurrencyDao();


    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public List<Currency> findAll() {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }

            return currencies;
        }

    }

    @Override
    @SneakyThrows
    public Optional<Currency> findByCode(String code) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {

            preparedStatement.setObject(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            Currency currency = null;

            if (resultSet.next()) {
                currency = buildCurrency(resultSet);
            }

            return Optional.ofNullable(currency);
        }
    }

    @Override
    @SneakyThrows
    public Currency save(Currency currency) {
        try (Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CURRENCY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, currency.getCode());
            preparedStatement.setObject(2, currency.getFullName());
            preparedStatement.setObject(3, currency.getSign());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            currency.setId(generatedKeys.getObject(1, Integer.class));

        }
        return currency;
    }

    @Override
    public void update(Currency entity) {

    }

    @SneakyThrows
    private Currency buildCurrency(ResultSet resultSet) {
        return Currency.builder()
                .id(resultSet.getObject("id", Integer.class))
                .code(resultSet.getObject("code", String.class))
                .fullName(resultSet.getObject("full_name", String.class))
                .sign(resultSet.getObject("sign", String.class))
                .build();
    }
}
