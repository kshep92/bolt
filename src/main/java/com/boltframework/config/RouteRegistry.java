package com.boltframework.config;

import com.boltframework.utils.Env;
import com.boltframework.web.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.omg.CORBA.Environment;

public class RouteRegistry extends RouteBuilder {
  private Vertx vertx;

  public RouteRegistry(Router router, Vertx vertx, PebbleEngine pebbleEngine, ObjectMapper mapper) {
    super(router);
    this.vertx = vertx;
    this.pebbleEngine = pebbleEngine;
    this.mapper = mapper;
  }

  public void mount(String path, Controller controller) {
    RouteBuilder builder = new RouteBuilder(Router.router(vertx));
    controller.doRoutes(builder);
    router.mountSubRouter(path, builder.getRouter());
  }

  public void staticFiles(String path, String directory) {
    StaticHandler handler = StaticHandler.create(directory).setDirectoryListing(false);
    Boolean enableCache = false;
    Long maxAge = 0L;
    String mode = Env.getString("app.mode") == null ? "development" : Env.getString("app.mode");
    assert mode != null;
    if(mode.matches("prod.*")) {
      enableCache = true;
      maxAge = 3600L;
    }
    StaticFilesConfiguration configuration = new StaticFilesConfiguration();
    configuration.setCacheEnabled(enableCache);
    configuration.setMaxAge(maxAge);
    router.get(path).handler(handler);
  }

  public void staticFiles(String path, String directory, StaticFilesConfiguration configuration) {
    StaticHandler handler = StaticHandler.create(directory);
    handler.setCachingEnabled(configuration.getCacheEnabled());
    handler.setMaxAgeSeconds(configuration.getMaxAge());
    router.get(path).handler(handler);
  }
}
