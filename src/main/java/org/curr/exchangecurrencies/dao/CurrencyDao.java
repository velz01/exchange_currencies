package org.curr.exchangecurrencies.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.curr.exchangecurrencies.entity.Currency;
import org.curr.exchangecurrencies.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyDao implements Dao<Currency> {
    private final String FIND_ALL = "SELECT * FROM currencies";
    private static final CurrencyDao INSTANCE = new CurrencyDao();



    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public List<Currency> findAll() {
       try (Connection connection = ConnectionManager.get()) {
           PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
       }
       return List.of();
    }

    @Override
    public Optional<Currency> findByCode() {
        return Optional.empty();
    }

    @Override
    public Currency save(Currency entity) {
        return null;
    }

    @Override
    public void update(Currency entity) {

    }
}
