package com.boltframework.data;

import java.util.HashMap;
import java.util.Map;

public class ConverterRegistry {

  private static Map<String, Converter<?>> converters = new HashMap<>();

  @SuppressWarnings("unchecked")
  public static <T> Converter<T> getConverter(Class<T> converterType) {
    return (Converter<T>) converters.get(converterType.getName());
  }

  public static <T> void add(Class<T> converterType, Converter<T> converter) {
    converters.put(converterType.getName(), converter);
  }
}
