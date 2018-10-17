package com.boltframework.data.converters;

import com.boltframework.data.Converter;

public class LongConverter implements Converter<Long> {
  @Override
  public Long convert(Object value) {
    return value == null ? null : Long.parseLong(value.toString());
  }
}
