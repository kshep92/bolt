package com.boltframework.utils.httpclient;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;

/**
 * A Handler to reroute POST requests that have to use the X-HTTP-METHOD-OVERRIDE header
 * because the client doesn't support PATCH and CONNECT methods. See {@link java.net.HttpURLConnection#setRequestMethod(String)}
 */
public class MethodOverrideHandler implements Handler<RoutingContext> {

  private Map<String, HttpMethod> nonStandardMethods = new HashMap<>();

  {
    nonStandardMethods.put("patch", HttpMethod.PATCH);
    nonStandardMethods.put("connect", HttpMethod.CONNECT);
  }

  @Override
  public void handle(RoutingContext ctx) {
    String intendedMethod = ctx.request().getHeader("X-HTTP-METHOD-OVERRIDE");
    if(intendedMethod != null && nonStandardMethods.get(intendedMethod.toLowerCase()) != null) {
      ctx.reroute(nonStandardMethods.get(intendedMethod.toLowerCase()), ctx.request().path());
    }
    else ctx.next();
  }
}
