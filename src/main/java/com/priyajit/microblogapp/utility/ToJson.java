package com.priyajit.microblogapp.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ToJson {
    public static String toJson(Object object) {
        String json = null;
        try {
            json = (new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
