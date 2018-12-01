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

  private String methodToOverride;
  private Map<String, HttpMethod> httpMethods = new HashMap<>();

  {
    httpMethods.put("PATCH", HttpMethod.PATCH);
    httpMethods.put("CONNECT", HttpMethod.CONNECT);
  }

  public MethodOverrideHandler(String methodToOverride) {
    this.methodToOverride = methodToOverride.toUpperCase();
  }

  @Override
  public void handle(RoutingContext ctx) {
    String intendedMethod = ctx.request().getHeader("X-HTTP-METHOD-OVERRIDE");
    if(intendedMethod != null && intendedMethod.toUpperCase().matches(methodToOverride)) {
      ctx.reroute(httpMethods.get(intendedMethod.toUpperCase()), ctx.request().path());
    }
    else ctx.next();
  }
}
