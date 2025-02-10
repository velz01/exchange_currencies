package org.curr.exchangecurrencies.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.curr.exchangecurrencies.dto.ExchangeDto;
import org.curr.exchangecurrencies.dto.ForExchangeDto;
import org.curr.exchangecurrencies.dto.ExchangeRatesDto;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.exception.ExchangeRatesNotFound;
import org.curr.exchangecurrencies.service.ExchangeRatesService;
import org.curr.exchangecurrencies.util.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ForExchangeDto forExchangeDto = buildExchangeDto(req);

        ExchangeDto exchangeDto = exchangeRatesService.obtainExchange(forExchangeDto);
        String json = JsonUtils.getJson(exchangeDto);
        resp.getWriter().write(json);

    }

    private static ForExchangeDto buildExchangeDto(HttpServletRequest req) {
        return ForExchangeDto.builder()
                .baseCurrency(req.getParameter("from"))
                .targetCurrency(req.getParameter("to"))
                .amount((req.getParameter("amount") != null) ? Integer.parseInt(req.getParameter("amount")) : -1)
                .build();
    }
}
