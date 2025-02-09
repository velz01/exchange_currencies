package org.curr.exchangecurrencies.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.curr.exchangecurrencies.dto.CreateCurrencyDto;
import org.curr.exchangecurrencies.dto.CurrencyDto;
import org.curr.exchangecurrencies.exception.CodeAlreadyExists;
import org.curr.exchangecurrencies.service.CurrencyService;
import org.curr.exchangecurrencies.util.ErrorType;
import org.curr.exchangecurrencies.util.JsonUtils;
import org.curr.exchangecurrencies.validator.CreateCurrencyDtoValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Enumeration;

@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyService currencyService = CurrencyService.getInstance();
    private final CreateCurrencyDtoValidator dtoValidator = CreateCurrencyDtoValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String json;
        try {
            json = JsonUtils.getJson(currencyService.findAll());
        } catch (SQLException e) {
            json = JsonUtils.getJson(ErrorType.INTERNAL_SERVER_ERROR);
        }
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String json;

        CreateCurrencyDto createCurrencyDto = buildCurrencyDto(req);
        if (dtoValidator.isValid(createCurrencyDto)) {
            try {
                CurrencyDto currencyDto = currencyService.create(createCurrencyDto);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                json = JsonUtils.getJson(currencyDto);
                resp.getWriter().write(json);
            } catch (CodeAlreadyExists e) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                json = JsonUtils.getJson(ErrorType.CURRENCY_ALREADY_EXISTS);
                resp.getWriter().write(json);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                json = JsonUtils.getJson(ErrorType.INTERNAL_SERVER_ERROR);
                resp.getWriter().write(json);
            }


        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            json = JsonUtils.getJson(ErrorType.FIELD_NOT_AVAILABLE);
            resp.getWriter().write(json);
        }

    }

    private static CreateCurrencyDto buildCurrencyDto(HttpServletRequest req) {
        return CreateCurrencyDto.builder()
                .code(req.getParameter("code"))
                .fullName(req.getParameter("name"))
                .sign(req.getParameter("sign"))
                .build();
    }
}
