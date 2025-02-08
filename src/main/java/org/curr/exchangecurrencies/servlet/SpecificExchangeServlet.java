package org.curr.exchangecurrencies.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.curr.exchangecurrencies.dto.ExchangeRatesDto;
import org.curr.exchangecurrencies.entity.ExchangeRates;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.exception.ExchangeRatesNotFound;
import org.curr.exchangecurrencies.service.ExchangeRatesService;
import org.curr.exchangecurrencies.util.ErrorType;
import org.curr.exchangecurrencies.util.JsonUtils;
import org.curr.exchangecurrencies.validator.CodeValidator;
import org.curr.exchangecurrencies.validator.ExchangeCodeValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class SpecificExchangeServlet extends HttpServlet {
   private final ExchangeCodeValidator validator = ExchangeCodeValidator.getInstance();
   private final ExchangeRatesService ratesService = ExchangeRatesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String codes = req.getRequestURI().replace("/exchangeRate/", "");
        System.out.println(codes);
        if (validator.isValid(codes)) {
            try {
                ExchangeRatesDto exchangeRatesDto = ratesService.findByCodes(codes);
                String json = JsonUtils.getJson(exchangeRatesDto);
                resp.getWriter().write(json);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                String json = JsonUtils.getJson(ErrorType.INTERNAL_SERVER_ERROR);
                resp.getWriter().write(json);
            } catch (CurrencyNotFoundException | ExchangeRatesNotFound e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                String json = JsonUtils.getJson(ErrorType.CURRENCY_NOT_FOUND);
                resp.getWriter().write(json);
            }
        }
        else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json = JsonUtils.getJson(ErrorType.CODE_NOT_FOUND);
            resp.getWriter().write(json);
        }
    }
}
