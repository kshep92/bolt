package com.boltframework.web.routing;

import io.vertx.ext.web.Router;

public class ExceptionHandlerBuilder extends AbstractRouteBuilder {

  private ExceptionHandlerProperties exceptionHandlerProperties;

  public ExceptionHandlerBuilder(ExceptionHandlerProperties exceptionHandlerProperties) {
    this.exceptionHandlerProperties = exceptionHandlerProperties;
  }

  @Override
  void buildRoutes(Router router) {
    configurePaths(router, exceptionHandlerProperties, route -> route.failureHandler(exceptionHandlerProperties.getHandler()));
  }
}
