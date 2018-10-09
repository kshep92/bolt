package com.boltframework.old;

import com.boltframework.web.HttpContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import static io.vertx.core.http.HttpMethod.*;

import java.util.function.Consumer;

@Deprecated
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

  private RouteBuilder createRoute(String path, HttpMethod method, Consumer<HttpContext> consumer) {
    router.route(method, path).handler(new RoutingContextHandler(consumer));
    return this;
  }

  public RouteBuilder get(String path, Consumer<HttpContext> consumer) {
    return createRoute(path, GET, consumer);
  }

  public RouteBuilder middleware(String path, Handler<RoutingContext> handler) {
   return middleware(HttpMethod.GET, path, handler);
  }

  public RouteBuilder middleware(HttpMethod method, String path, Handler<RoutingContext> handler) {
    router.route(method, path).handler(handler);
    return this;
  }

  public RouteBuilder get(Consumer<HttpContext> consumer) {
    return get("/", consumer);
  }

  public RouteBuilder post(String path, Consumer<HttpContext> consumer) {
    return createRoute(path, POST, consumer);
  }

  public RouteBuilder post(Consumer<HttpContext> consumer) {
    return post("/",consumer);
  }

  public RouteBuilder put(String path, Consumer<HttpContext> consumer) {
    return createRoute(path, PUT, consumer);
  }

  public RouteBuilder put(Consumer<HttpContext> consumer) {
    return put("/", consumer);
  }

  public RouteBuilder delete(String path, Consumer<HttpContext> consumer) {
    return createRoute(path, DELETE, consumer);
  }

  public RouteBuilder delete(Consumer<HttpContext> consumer) {
    return delete("/", consumer);
  }

  public RouteBuilder use(Consumer<HttpContext> consumer) {
    router.route().handler(new RoutingContextHandler(consumer));
    return this;
  }

  public RouteBuilder use(String path, Consumer<HttpContext> consumer) {
    router.route(path).handler(new RoutingContextHandler(consumer));
    return this;
  }

  public RouteBuilder useRegex(String expression, Consumer<HttpContext> consumer) {
    router.routeWithRegex(expression).handler(new RoutingContextHandler(consumer));
    return this;
  }

  private class RoutingContextHandler implements Handler<RoutingContext> {

    private Consumer<HttpContext> consumer;

    RoutingContextHandler(Consumer<HttpContext> consumer) { this.consumer = consumer; }

    public void handle(RoutingContext context) {
      HttpContext routeContext = new HttpContext();
      routeContext.setDelegate(context);
      consumer.accept(routeContext);
    }
  }

}
