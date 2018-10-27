package com.boltframework.web.routing;

import com.boltframework.web.HttpContext;

import java.util.function.Consumer;

public class InterceptorCollection {

  public InterceptorProperties addInterceptor(Consumer<HttpContext> interceptor) {
    InterceptorProperties interceptorProperties = new InterceptorProperties(interceptor);
    return PropertiesRegistry.addInterceptorProperties(interceptorProperties);
  }

}
