package org.curr.exchangecurrencies.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final String MESSAGE = "message";

    public static String getJson(String error) throws JsonProcessingException {
        Map<String, String> message = Map.of(MESSAGE, error);
        return jsonMapper.writeValueAsString(message);
    }

    public static String getJson(Object dto) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(dto);
    }

}
