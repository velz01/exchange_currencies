package org.curr.exchangecurrencies.dao;

import org.curr.exchangecurrencies.exception.CodeAlreadyExists;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll() throws SQLException;
    Optional<T> findByCode(String code) throws SQLException;
    T save(T entity) throws SQLException, CodeAlreadyExists;
    void update(T entity);

}
