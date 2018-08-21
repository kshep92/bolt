package com.boltframework.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Json {

  public static String stringify(Object object) {
    try {
      return objectMapper.writer().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> T parse(String json, Class<T> aClass) {
    try {
      return objectMapper.readValue(json, aClass);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static ObjectMapper objectMapper = new ObjectMapper();
}
