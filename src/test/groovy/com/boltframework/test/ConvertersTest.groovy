package com.boltframework.test

import com.boltframework.data.Converter
import com.boltframework.data.ConverterRegistry
import com.boltframework.data.converters.BooleanConverter
import com.boltframework.data.converters.DoubleConverter
import com.boltframework.data.converters.IntConverter
import com.boltframework.data.converters.LongConverter
import com.boltframework.data.converters.StringConverter
import org.junit.Test

import static org.junit.Assert.*

class ConvertersTest {

  @Test
  public void 'converter registry'() {
    ConverterRegistry.add(Long.class, new LongConverter())
    String longString = '100'
    Converter<Long> longConverter = ConverterRegistry.getConverter(Long.class)
    assertNotNull(longConverter)
    assertEquals(new Long(100), longConverter.convert(longString))
  }

  @Test
  public void 'boolean converter'() {
    def converter = new BooleanConverter()
    String someBool = 'true'
    assertTrue(converter.convert(someBool))
    assertFalse(converter.convert('false'))
  }

  @Test
  public void 'double converter'() {
    def converter = new DoubleConverter()
    String someDouble = '1.00'
    assertTrue(new Double(1.00) == (converter.convert(someDouble)))
  }

  @Test
  public void 'int converter'() {
    def converter = new IntConverter()
    String someInt = '1'
    assertEquals(new Integer(1), converter.convert(someInt))
  }

  @Test
  public void 'long converter'() {
    def converter = new LongConverter()
    String someLong = '1'
    assertEquals(new Long(1), converter.convert(someLong))
  }

  @Test
  public void 'string converter test'() {
    def converter = new StringConverter()
    int someInt = 1
    assertEquals('1', converter.convert(someInt))
  }


}
