package org.curr.exchangecurrencies.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll();
    Optional<T> findByCode();
    T save(T entity);
    void update(T entity);

}
