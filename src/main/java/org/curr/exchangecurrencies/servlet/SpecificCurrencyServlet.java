package org.curr.exchangecurrencies.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.service.CurrencyService;
import org.curr.exchangecurrencies.util.ErrorType;
import org.curr.exchangecurrencies.util.JsonUtils;
import org.curr.exchangecurrencies.validator.CodeValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/currency/*")
public class SpecificCurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final CodeValidator codeValidator = CodeValidator.getInstance();



    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String code = req.getRequestURI().replace("/currency/", "");
        String json;
        if (codeValidator.isValid(code)) {
            try {
                CurrencyDto currency = currencyService.findByCode(code);

                json = JsonUtils.getJson(currency);
                resp.getWriter().write(json);
            } catch (CurrencyNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                json = JsonUtils.getJson(ErrorType.CURRENCY_NOT_FOUND);
                resp.getWriter().write(json);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                json = JsonUtils.getJson(ErrorType.INTERNAL_SERVER_ERROR);
                resp.getWriter().write(json);
            }


        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            json = JsonUtils.getJson(ErrorType.CODE_NOT_FOUND);
            resp.getWriter().write(json);
        }
    }
}
