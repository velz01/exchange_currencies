package org.curr.exchangecurrencies.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorType {
    public static final String CODE_NOT_FOUND = "Код валюты отсутствует в адресе";
    public static final String CURRENCY_NOT_FOUND = "Валюта не найдена";
    public static final String FIELD_NOT_AVAILABLE = "Отсутствует нужное поле формы";
    public static final String INTERNAL_SERVER_ERROR = "Ошибка";
    public static final String CURRENCY_ALREADY_EXISTS = "Валюта с таким кодом уже существует";
    public static final String EXCHANGE_ALREADY_EXISTS = "Валютная пара с таким кодом уже существует";
}
