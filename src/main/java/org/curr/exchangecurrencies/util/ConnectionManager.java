package org.curr.exchangecurrencies.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;

@UtilityClass
public class ConnectionManager {
    private final String URL_KEY =  "jdbc:sqlite:D:/projectsprog/ExchangeCurrencies/src/main/resources/currencies";

    static {
        loadDriver();
    }

    @SneakyThrows
    private void loadDriver() {
        Class.forName("org.sqlite.JDBC");
    }

    @SneakyThrows
    public Connection get() {
        return DriverManager.getConnection(URL_KEY);
    }
}
