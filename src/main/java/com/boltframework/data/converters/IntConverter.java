package com.boltframework.data.converters;

import com.boltframework.data.Converter;

public class IntConverter implements Converter<Integer> {
  @Override
  public Integer convert(Object value) {
    return value == null ? null : Integer.parseInt(value.toString());
  }
}
