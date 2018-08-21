package com.boltframework.config;

import com.boltframework.web.RouteContext;
import com.boltframework.web.impl.RouteContextImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import static io.vertx.core.http.HttpMethod.*;

import java.util.function.Consumer;

public class RouteBuilder {
  Router router;
  PebbleEngine pebbleEngine;
  ObjectMapper mapper;

  RouteBuilder(Router router) {
    this.router = router;
  }

  Router getRouter() {
    return router;
  }

  private RouteBuilder createRoute(String path, HttpMethod method, Consumer<RouteContext> consumer) {
    router.route(method, path).handler(new RoutingContextHandler(consumer));
    return this;
  }

  public RouteBuilder get(String path, Consumer<RouteContext> consumer) {
    return createRoute(path, GET, consumer);
  }

  public RouteBuilder middleware(String path, Handler<RoutingContext> handler) {
   return middleware(HttpMethod.GET, path, handler);
  }

  public RouteBuilder middleware(HttpMethod method, String path, Handler<RoutingContext> handler) {
    router.route(method, path).handler(handler);
    return this;
  }

  public RouteBuilder get(Consumer<RouteContext> consumer) {
    return get("/", consumer);
  }

  public RouteBuilder post(String path, Consumer<RouteContext> consumer) {
    return createRoute(path, POST, consumer);
  }

  public RouteBuilder post(Consumer<RouteContext> consumer) {
    return post("/",consumer);
  }

  public RouteBuilder put(String path, Consumer<RouteContext> consumer) {
    return createRoute(path, PUT, consumer);
  }

  public RouteBuilder put(Consumer<RouteContext> consumer) {
    return put("/", consumer);
  }

  public RouteBuilder delete(String path, Consumer<RouteContext> consumer) {
    return createRoute(path, DELETE, consumer);
  }

  public RouteBuilder delete(Consumer<RouteContext> consumer) {
    return delete("/", consumer);
  }

  public RouteBuilder use(Consumer<RouteContext> consumer) {
    router.route().handler(new RoutingContextHandler(consumer));
    return this;
  }

  public RouteBuilder use(String path, Consumer<RouteContext> consumer) {
    router.route(path).handler(new RoutingContextHandler(consumer));
    return this;
  }

  public RouteBuilder useRegex(String expression, Consumer<RouteContext> consumer) {
    router.routeWithRegex(expression).handler(new RoutingContextHandler(consumer));
    return this;
  }

  private class RoutingContextHandler implements Handler<RoutingContext> {

    private Consumer<RouteContext> consumer;

    RoutingContextHandler(Consumer<RouteContext> consumer) { this.consumer = consumer; }

    public void handle(RoutingContext context) {
      RouteContext routeContext = new RouteContextImpl(context, pebbleEngine, mapper);
      consumer.accept(routeContext);
    }
  }

}
