package com.boltframework.web.routing;

import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.util.ArrayList;
import java.util.List;

public class RouteBuilder extends AbstractRouteBuilder {

  private RouteProperties routeProperties;

  public RouteBuilder(RouteProperties routeProperties) {
    this.routeProperties = routeProperties;
  }

  @Override
  public void buildRoutes(Router router) {
    List<Route> routes = new ArrayList<>();
    if(!routeProperties.getPatterns().isEmpty()) {
      for(String pattern: routeProperties.getPatterns()) {
        logger.debug("Creating route {}", tablulate(routeProperties.getMethod().name(), pattern));
        Route route = router.routeWithRegex(routeProperties.getMethod(), pattern);
        routes.add(route);
      }
    } else {
      for(String path: routeProperties.getPaths()) {
            logger.debug("Creating route {}", tablulate(routeProperties.getMethod().name(), path));
            Route route = router.route(routeProperties.getMethod(), path);
            routes.add(route);
          }
    }
    for(Route route : routes) {
      configureMimeTypes(route, routeProperties);
      if(routeProperties.isBlocking()) route.blockingHandler(routeProperties.getHandler());
      else route.handler(routeProperties.getHandler());
    }
  }

  private String tablulate(String method, String path) {
    return String.format("%-10s %s", method.toUpperCase(), path);
  }
}
