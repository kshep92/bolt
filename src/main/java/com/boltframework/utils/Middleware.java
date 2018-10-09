package com.boltframework.utils;

import com.boltframework.web.HttpContext;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.function.Consumer;

public class Middleware {

  public static Consumer<HttpContext> convertVertxHandler(Handler<RoutingContext> handler) {
    return ctx -> handler.handle(ctx.getDelegate());
  }
}
