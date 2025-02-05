package org.curr.exchangecurrencies.validator;

public interface Validator<T> {
    boolean isValid(T obj);
}
