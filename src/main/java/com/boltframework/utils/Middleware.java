package com.boltframework.utils;

import com.boltframework.web.mvc.Interceptor;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class Middleware {

  public static Interceptor convertVertxHandler(Handler<RoutingContext> handler) {
    return ctx -> handler.handle(ctx.getDelegate());
  }
}
