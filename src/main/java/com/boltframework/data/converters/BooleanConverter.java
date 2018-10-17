package com.boltframework.data.converters;

import com.boltframework.data.Converter;

public class BooleanConverter implements Converter<Boolean> {
  @Override
  public Boolean convert(Object value) {
    return value != null && Boolean.parseBoolean(value.toString());
  }
}
