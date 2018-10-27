package com.boltframework.web.mvc;

import com.boltframework.web.HttpContext;
import com.boltframework.web.HttpRequest;
import com.boltframework.web.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Controller {

  private ThreadLocal<HttpContext> httpContext = new ThreadLocal<>();
//  private HttpContext _context;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  public void clearContext() {
    httpContext.remove();
  }

  public Controller withHttpContext(HttpContext httpContext) {
    this.httpContext.set(httpContext);
//    this._context = httpContext;
    return this;
  }

  public ThreadLocal<HttpContext> getHttpContext() {
    return httpContext;
  }

  public HttpContext context() {
    return httpContext.get();
//    return _context;
  }

  public HttpRequest request() {
    return context().getRequest();
  }

  public HttpResponse response() {
    return context().getResponse();
  }
}
