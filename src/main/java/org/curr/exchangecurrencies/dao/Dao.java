package org.curr.exchangecurrencies.dao;

import org.curr.exchangecurrencies.exception.CodeAlreadyExists;
import org.curr.exchangecurrencies.exception.ExchangeRatesAlreadyExists;
import org.curr.exchangecurrencies.exception.ExchangeRatesNotFound;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll() throws SQLException;

    T save(T entity) throws SQLException, CodeAlreadyExists, ExchangeRatesAlreadyExists;
    void update(T entity) throws SQLException, ExchangeRatesNotFound;

}
