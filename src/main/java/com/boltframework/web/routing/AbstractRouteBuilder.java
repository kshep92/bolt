package com.boltframework.web.routing;

import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

abstract class AbstractRouteBuilder {

  void buildRoutes(Router router) {}

  protected Logger logger = LoggerFactory.getLogger(getClass());

  void configurePaths(Router router, AbstractRouteProperties routeProperties, Consumer<Route> callback) {
    if(routeProperties.getPaths().isEmpty() && routeProperties.getPatterns().isEmpty()) {
      callback.accept(router.route());
      return;
    }
    for(String path : routeProperties.getPaths()) {
      callback.accept(router.route(path));
    }
    for(String pattern : routeProperties.getPatterns()) {
      callback.accept(router.routeWithRegex(pattern));
    }
  }

  void configureMimeTypes(Route route, AbstractRouteProperties routeProperties) {
    for(String entry : routeProperties.getConsumes()) {
      route.consumes(entry);
    }
    for(String entry : routeProperties.getProduces()) {
      route.produces(entry);
    }
  }
}
