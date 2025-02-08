package org.curr.exchangecurrencies.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.curr.exchangecurrencies.dto.CreateExchangeDto;
import org.curr.exchangecurrencies.dto.ExchangeRatesDto;
import org.curr.exchangecurrencies.exception.CodeAlreadyExists;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.exception.ExchangeRatesAlreadyExists;
import org.curr.exchangecurrencies.service.ExchangeRatesService;
import org.curr.exchangecurrencies.util.ErrorType;
import org.curr.exchangecurrencies.util.JsonUtils;
import org.curr.exchangecurrencies.validator.CreateExchangeRatesValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
    private final CreateExchangeRatesValidator validator = CreateExchangeRatesValidator.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String json;
        try {
            List<ExchangeRatesDto> exchangeRatesDtos = exchangeRatesService.findAll();
            json = JsonUtils.getJson(exchangeRatesDtos);
        } catch (SQLException | CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json = JsonUtils.getJson(ErrorType.INTERNAL_SERVER_ERROR);

        }
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String json;
        CreateExchangeDto dto = buildCreateExchangeDto(req);
        if (validator.isValid(dto)) {

        try {
            ExchangeRatesDto exchangeRatesDtos = exchangeRatesService.save(dto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            json = JsonUtils.getJson(exchangeRatesDtos);
        } catch (SQLException e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json = JsonUtils.getJson(ErrorType.INTERNAL_SERVER_ERROR);

        } catch (CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            json = JsonUtils.getJson(ErrorType.CURRENCY_NOT_FOUND);

        } catch (ExchangeRatesAlreadyExists e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            json = JsonUtils.getJson(ErrorType.EXCHANGE_ALREADY_EXISTS);
        }

        }
        else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            json = JsonUtils.getJson(ErrorType.FIELD_NOT_AVAILABLE);

        }
        resp.getWriter().write(json);
    }

    private static CreateExchangeDto buildCreateExchangeDto(HttpServletRequest req) {
        return CreateExchangeDto.builder()
                .baseCurrency(req.getParameter("baseCurrencyCode"))
                .targetCurrency(req.getParameter("targetCurrencyCode"))
                .rate(((req.getParameter("rate") != null) ? Float.parseFloat(req.getParameter("rate")) : (float) -1))
                .build();

    }
}
