package com.boltframework.web.routing;

import com.boltframework.web.HttpContext;

import java.util.function.Consumer;

public class ExceptionHandlerCollection {

  public ExceptionHandlerProperties addHandler(Consumer<HttpContext> handler) {
    ExceptionHandlerProperties properties = new ExceptionHandlerProperties(handler);
    return PropertiesRegistry.addExceptionHandlerProperties(properties);
  }

}
