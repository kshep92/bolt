package com.boltframework.data;

/**
 * A registry of data converters
 */
public interface Converter<T> {
  T convert(Object value);
}
