package com.yashubale.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class SerDeserUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setDateFormat(new SimpleDateFormat("MM/dd/yyyy"));
    }


    public static <T> byte[] writeValueAsBytes(T t) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(t);
    }


    public static <T> String writeValueAsString(T t) throws JsonProcessingException {
        return objectMapper.writeValueAsString(t);
    }

    public static <T> T readValue(File file, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(file, typeReference);
    }

    public static <T> T readValue(byte[] bytes, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(bytes, typeReference);
    }
}
