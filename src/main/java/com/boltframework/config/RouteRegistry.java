package com.boltframework.config;

import com.boltframework.web.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class RouteRegistry extends RouteBuilder {
  private Vertx vertx;

  public RouteRegistry(Router router, Vertx vertx, PebbleEngine pebbleEngine, ObjectMapper mapper) {
    super(router);
    this.vertx = vertx;
    this.pebbleEngine = pebbleEngine;
    this.mapper = mapper;
  }

  public void use(String path, Controller controller) {
    RouteBuilder builder = new RouteBuilder(Router.router(vertx));
    controller.doRoutes(builder);
    router.mountSubRouter(path, builder.getRouter());
  }
}
