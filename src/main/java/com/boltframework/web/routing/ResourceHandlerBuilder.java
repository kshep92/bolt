package com.boltframework.web.routing;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class ResourceHandlerBuilder extends AbstractRouteBuilder {

  private ResourceHandlerProperties properties;

  public ResourceHandlerBuilder(ResourceHandlerProperties properties) {
    this.properties = properties;
  }

  @Override
  public void buildRoutes(Router router) {
    StaticHandler staticHandler = StaticHandler.create(properties.getWebRoot())
        .setCachingEnabled(properties.getCache()).setMaxAgeSeconds(properties.getMaxAge());
    router.route(properties.getPath()).handler(staticHandler);
  }
}
