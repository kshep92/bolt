package com.boltframework.web.routing;

import io.vertx.ext.web.Router;

public class InterceptorBuilder extends AbstractRouteBuilder {

  private InterceptorProperties interceptorProperties;

  public InterceptorBuilder(InterceptorProperties interceptorRouteProperties) {
    this.interceptorProperties = interceptorRouteProperties;
  }

  public void buildRoutes(Router router) {
    configurePaths(router, interceptorProperties, route -> {
      configureMimeTypes(route, interceptorProperties);
      route.handler(interceptorProperties.getHandler());
    });
  }
}
