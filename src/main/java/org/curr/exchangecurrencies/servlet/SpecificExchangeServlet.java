package org.curr.exchangecurrencies.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.curr.exchangecurrencies.dto.CreateExchangeDto;
import org.curr.exchangecurrencies.dto.ExchangeRatesDto;
import org.curr.exchangecurrencies.entity.ExchangeRates;
import org.curr.exchangecurrencies.exception.CurrencyNotFoundException;
import org.curr.exchangecurrencies.exception.ExchangeRatesNotFound;
import org.curr.exchangecurrencies.service.ExchangeRatesService;
import org.curr.exchangecurrencies.util.ErrorType;
import org.curr.exchangecurrencies.util.JsonUtils;
import org.curr.exchangecurrencies.validator.CodeValidator;
import org.curr.exchangecurrencies.validator.CreateExchangeRatesValidator;
import org.curr.exchangecurrencies.validator.ExchangeCodeValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

@WebServlet("/exchangeRate/*")
public class SpecificExchangeServlet extends HttpServlet {
   private final ExchangeCodeValidator exchangeCodeValidator = ExchangeCodeValidator.getInstance();
   private final CreateExchangeRatesValidator createExchangeRatesValidator = CreateExchangeRatesValidator.getInstance();
   private final ExchangeRatesService ratesService = ExchangeRatesService.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("rate"));
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req,resp);
        }
        else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String codes = req.getRequestURI().replace("/exchangeRate/", "");

        if (exchangeCodeValidator.isValid(codes)) {
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


    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");


        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setStatus(HttpServletResponse.SC_OK);

        String codes = req.getRequestURI().replace("/exchangeRate/", "");
        if (exchangeCodeValidator.isValid(codes)) {
            try {
                CreateExchangeDto dto = buildCreateExchangeDto(req, codes);
                if (createExchangeRatesValidator.isValid(dto)) {

                    ExchangeRatesDto exchangeRatesDto = ratesService.update(dto);
                    String json = JsonUtils.getJson(exchangeRatesDto);
                    resp.getWriter().write(json);
                }
                else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    String json = JsonUtils.getJson(ErrorType.FIELD_NOT_AVAILABLE);
                    resp.getWriter().write(json);
                }
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
    private static Float getRate(HttpServletRequest req) throws IOException {
        String[] split = req.getReader().lines().collect(Collectors.joining()).split("=");
        float rate;
        if (Objects.equals(split[0], "rate")) {
            rate = Float.parseFloat(split[1]);
//            System.out.println(Long.parseLong(split[1]));
//            System.out.println(rate);
        }
        else {
            rate = (float) -1;
        }
        return rate;
    }
    private static CreateExchangeDto buildCreateExchangeDto(HttpServletRequest req, String codes) throws IOException {

        String baseCode = codes.substring(0,3);
        String targetCode = codes.substring(3,6);
        return CreateExchangeDto.builder()
                .baseCurrency(baseCode)
                .targetCurrency(targetCode)
                .rate(getRate(req))
                .build();

    }
}
