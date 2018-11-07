package com.boltframework.web;

import com.boltframework.data.Converter;
import com.boltframework.data.ConverterRegistry;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Map;
import java.util.Set;

public class HttpContext {

  private RoutingContext delegate;
  private ObjectMapper objectMapper;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public HttpContext() {
    //TODO: Configure the mapper externally and and inject.
    objectMapper = Json.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDateFormat(DateFormat.getDateInstance(DateFormat.SHORT));
  }

  /**
   * Send a one-off message to the user via a "flash" cookie.
   * @param message The message to send.
   * @return the current HttpContext
   */
  public HttpContext flash(String message) {
    try {
      delegate.addCookie(Cookie.cookie("flash", URLEncoder.encode(message, StandardCharsets.UTF_8.name())).setPath("/").setMaxAge(1));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return this;
  }

  public HttpContext withDelegate(RoutingContext context) {
    this.delegate = context;
    return this;
  }

  public RoutingContext getDelegate() {
    return delegate;
  }

  public HttpContext addCookie(Cookie cookie) {
    delegate.addCookie(cookie);
    return this;
  }

  public HttpContext addCookie(String name, String value) {
    try {
      Cookie cookie = Cookie.cookie(name, URLEncoder.encode(value, "utf-8")).setMaxAge(3600);
      return this.addCookie(cookie);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }

  }

  public void fail(Integer statusCode) {
    delegate.fail(statusCode);
  }

  public Object get(String key) {
    return delegate.get(key);
  }

  public <T> T getBodyAs(Class<T> bodyType) {
    //TODO: Cater for application/x-www-form-url-encoded and multipart/form-data
    if(!getRequest().contentTypeMatches("json")) return null;
    T result = null;
    try {
      result = objectMapper.readValue(getBodyAsJson().encode(), bodyType);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public JsonObject getBodyAsJson() {
    return delegate.getBodyAsJson();
  }

  public String getBodyAsString() {
    return delegate.getBodyAsString();
  }

  public Cookie getCookie(String name) {
    return delegate.getCookie(name);
  }

  public Set<Cookie> getCookies() {
    return delegate.cookies();
  }

  public Set<FileUpload> getFileUploads() {
    return delegate.fileUploads();
  }

  public String getHeader(String name) {
    return delegate.request().getHeader(name);
  }

  public String getPathParam(String name) {
    return delegate.pathParam(name);
  }

  @Nullable
  public <T> T getPathParam(String name, Class<T> type) {
    Converter<T> converter = ConverterRegistry.getConverter(type);
    if(converter == null) {
      logger.error("No converter found for class {}", type);
      return null;
    }
    return converter.convert(delegate.pathParam(name));
  }

  public Map<String, String> getPathParams() {
    return delegate.pathParams();
  }

  public HttpRequest getRequest() {
    return new HttpRequest(delegate.request());
  }

  public HttpResponse getResponse() {
    return new HttpResponse(delegate.response(), objectMapper);
  }

  public void next() {
    delegate.next();
  }

  public HttpContext put(String key, Object value) {
    delegate.put(key, value);
    return this;
  }

  public HttpContext remove(String key) {
    delegate.data().remove(key);
    return this;
  }

  public HttpContext removeCookie(String name) {
    Cookie cookie = delegate.getCookie(name);
    if(cookie == null) return this;
    delegate.removeCookie(name); // Remove it from the context
    String path = cookie.getPath() == null ? "/" : cookie.getPath();
    delegate.addCookie(cookie.setPath(path).setMaxAge(0));
    return this;
  }

  public void reroute(String path) {
    delegate.reroute(path);
  }
}