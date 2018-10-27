package com.boltframework.web.routing;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractRouteProperties {
  private Set<String> paths = new HashSet<>();
  private Set<String> patterns = new HashSet<>();
  private Handler<RoutingContext> handler;
  private Set<String> consumes = new HashSet<>();
  private Set<String> produces = new HashSet<>();

  protected AbstractRouteProperties addPath(String path) {
    this.paths.add(formatPath(path));
    return this;
  }

  protected AbstractRouteProperties addPathRegex(String regex) {
    this.patterns.add(regex);
    return this;
  }

  protected void setHandler(Handler<RoutingContext> handler) {
    this.handler = handler;
  }

  public Handler<RoutingContext> getHandler() {
    return handler;
  }

  Set<String> getPaths() {
    return paths;
  }

  Set<String> getPatterns() {
    return patterns;
  }

  Set<String> getConsumes() {
    return consumes;
  }

  Set<String> getProduces() {
    return produces;
  }

  /**
   * Return a path segment that matches "/{path}"
   * @param input The path segment to format
   * @return A formatted path segment
   */
  static String formatPath(String input) {
    if (input.startsWith("/")) return input;
    return "/" + input;
  }
}
