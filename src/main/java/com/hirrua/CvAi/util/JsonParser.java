package com.hirrua.CvAi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirrua.CvAi.dto.AnaliseResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {

    private static String cleanJson;

    public static AnaliseResponse parserJson(String json) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Pattern pattern = Pattern.compile("```json(.*?)```", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                cleanJson = matcher.group(1).trim();
            }

            JsonNode jsonNode = objectMapper.readTree(cleanJson);
            return objectMapper.treeToValue(jsonNode, AnaliseResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Falha ao processar Json");
        }
    }
}
