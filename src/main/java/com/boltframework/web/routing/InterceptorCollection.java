package com.boltframework.web.routing;

import com.boltframework.web.mvc.Interceptor;

public class InterceptorCollection {

  public InterceptorProperties addInterceptor(Interceptor interceptor) {
    InterceptorProperties interceptorProperties = new InterceptorProperties(interceptor);
    return PropertiesRegistry.addInterceptorProperties(interceptorProperties);
  }

}
