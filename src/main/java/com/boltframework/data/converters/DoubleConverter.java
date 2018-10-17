package com.boltframework.data.converters;

import com.boltframework.data.Converter;

public class DoubleConverter implements Converter<Double> {
  @Override
  public Double convert(Object value) {
    return value == null ?  null : Double.parseDouble(value.toString());
  }
}
