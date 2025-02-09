package org.curr.exchangecurrencies.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.curr.exchangecurrencies.dto.ForExchangeDto;
import org.curr.exchangecurrencies.dto.ExchangeRatesDto;
import org.curr.exchangecurrencies.service.ExchangeRatesService;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ForExchangeDto forExchangeDto = buildExchangeDto(req);
        ExchangeRatesDto exchangeRatesDto = exchangeRatesService.obtainExchange(forExchangeDto);
    }

    private static ForExchangeDto buildExchangeDto(HttpServletRequest req) {
        return ForExchangeDto.builder()
                .baseCurrency(req.getParameter("from"))
                .targetCurrency(req.getParameter("to"))
                .amount((req.getParameter("amount") != null) ? Integer.parseInt(req.getParameter("amount")) : -1)
                .build();
    }
}
