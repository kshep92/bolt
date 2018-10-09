package com.boltframework.utils.httpclient;

import com.boltframework.utils.Strings;

import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class HttpRequest extends HttpEntity {
  private String method;
  private HashMap<String, String> queryParams = new HashMap<>();

  public static HttpRequest get(String path) {
    return new HttpRequest().method("GET").path(path);
  }

  public static HttpRequest post(String path) {
    return new HttpRequest().method("POST").path(path);
  }

  public static HttpRequest patch(String path) {
    return new HttpRequest().method("PATCH").path(path);
  }

  public static HttpRequest put(String path) {
    return new HttpRequest().method("PUT").path(path);
  }

  public static HttpRequest delete(String path) {
    return new HttpRequest().method("DELETE").path(path);
  }

  public HttpRequest path(String path) {
    this.path = path;
    return this;
  }

  public HttpRequest method(String method) {
    this.method = method;
    return this;
  }

  public HttpRequest body(String body) {
    this.body = body;
    return this;
  }

  public HttpRequest header(String key, Object value) {
    addHeader(key, value.toString());
    return this;
  }

  public HttpRequest cookie(String name, String value) {
    Cookie cookie = Cookie.create(name, value);
    getCookies().put(name, cookie);
    return this;
  }

  public HttpRequest query(String key, Object value) {
    queryParams.put(key, value.toString());
    return this;
  }

  public HttpRequest contentType(String contentType) {
    addHeader("Content-Type", contentType);
    return this;
  }

  public HttpRequest json(String body) {
    return contentType("application/json").body(body);
  }

  @Override
  public String getPath() {
    if(!queryParams.isEmpty()) path = path.concat("?").concat(getQueryString());
    return path;
  }

  public String getMethod() {
    return method;
  }

  @Override
  public String toString() {
    String template = "# HttpRequest\nmethod: %s\n%s\nquery: %s\n";
    return String.format(template, method, super.toString(), getQueryString());
  }

  private String getQueryString() {
    StringBuilder sb = new StringBuilder();
    String queryString;
    queryParams.forEach((key, value) -> sb.append(String.format("%s=%s&", key, value)));
    queryString = sb.toString();
    if(sb.length() > 0)
      queryString = Strings.trimEnd(queryString); // Remove the trailing &
    return queryString;
  }
}
