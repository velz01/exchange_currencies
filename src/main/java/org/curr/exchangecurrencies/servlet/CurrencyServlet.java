package org.curr.exchangecurrencies.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.curr.exchangecurrencies.dto.CreateCurrencyDto;
import org.curr.exchangecurrencies.service.CurrencyService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        resp.getWriter().write(currencyService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String json = currencyService.create(buildCurrencyDto(req));
        resp.getWriter().write(json);

    }

    private static CreateCurrencyDto buildCurrencyDto(HttpServletRequest req) {
        return CreateCurrencyDto.builder()
                .code(req.getParameter("code"))
                .fullName(req.getParameter("full_name"))
                .sign(req.getParameter("sign"))
                .build();
    }
}
