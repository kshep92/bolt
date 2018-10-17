package com.boltframework.data.converters;

import com.boltframework.data.Converter;

public class StringConverter implements Converter<String> {
  @Override
  public String convert(Object value) {
    return value == null ? null : value.toString();
  }
}
