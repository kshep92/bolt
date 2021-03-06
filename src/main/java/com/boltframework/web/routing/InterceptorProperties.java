package com.boltframework.web.routing;


import com.boltframework.context.ApplicationContext;
import com.boltframework.web.HttpContext;
import com.boltframework.web.mvc.Interceptor;

public class InterceptorProperties extends AbstractRouteProperties {

  InterceptorProperties(Interceptor handler) {
    setHandler(ctx -> handler.accept(ApplicationContext.getBean(HttpContext.class).withDelegate(ctx)));
  }

  public InterceptorProperties consumes(String mimeType) {
    getConsumes().add(mimeType);
    return this;
  }

  public InterceptorProperties produces(String mimeType) {
    getProduces().add(mimeType);
    return this;
  }

  public InterceptorProperties addPath(String path) {
    return (InterceptorProperties) super.addPath(path);
  }

  public InterceptorProperties addPathRegex(String regex) {
    return (InterceptorProperties) super.addPathRegex(regex);
  }
}
