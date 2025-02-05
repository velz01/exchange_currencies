package org.curr.exchangecurrencies.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.service.CurrencyService;
import org.curr.exchangecurrencies.validator.CurrencyValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/currency/*")
public class SpecificCurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final CurrencyValidator currencyValidator = CurrencyValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String code = req.getRequestURI().replace("/currency/", "");
        if (currencyValidator.isValid(code)) {
            String json = null;
            try {
                json = currencyService.findByCode(code);
                resp.getWriter().write(json);
            } catch (CurrencyNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);


        }
    }
}
