package com.boltframework.web.routing;

import com.boltframework.web.HttpContext;

import java.util.function.Consumer;

public class ExceptionHandlerProperties extends AbstractRouteProperties {

  ExceptionHandlerProperties(Consumer<HttpContext> handler) {
    setHandler(ctx -> handler.accept(new HttpContext().withDelegate(ctx)));
  }

  public ExceptionHandlerProperties addPath(String path) {
    return (ExceptionHandlerProperties) super.addPath(path);
  }

  public ExceptionHandlerProperties addPathRegex(String regex) {
    return (ExceptionHandlerProperties) super.addPathRegex(regex);
  }
}
