package org.curr.exchangecurrencies.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.curr.exchangecurrencies.dto.ExchangeCurrencyDto;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.service.ExchangeCurrencyService;
import org.curr.exchangecurrencies.util.ErrorType;
import org.curr.exchangecurrencies.util.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeCurrencyServlet extends HttpServlet {
    ExchangeCurrencyService exchangeCurrencyService = ExchangeCurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String json;
        try {
            List<ExchangeCurrencyDto> exchangeCurrencyDtos = exchangeCurrencyService.findAll();
             json = JsonUtils.getJson(exchangeCurrencyDtos);
        } catch (SQLException | CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             json = JsonUtils.getJson(ErrorType.INTERNAL_SERVER_ERROR);

        }
        resp.getWriter().write(json);
    }
}
