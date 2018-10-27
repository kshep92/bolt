package com.boltframework.utils.httpclient;

import com.boltframework.utils.Json;
import com.boltframework.utils.Strings;

import java.util.*;

/**
 * Class housing common functionality for {@link HttpRequest} and {@link HttpResponse}
 */
@SuppressWarnings("WeakerAccess")
public abstract class HttpEntity {
  HashMap<String, List<String>> headers = new HashMap<>();
  String body = "";
  String path;
  private HashMap<String, Cookie> cookies = new HashMap<>();

  public HashMap<String, Cookie> getCookies() {
    return cookies;
  }

  public void setCookies(HashMap<String, Cookie> cookies) {
    this.cookies = cookies;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public HashMap<String, List<String>> getHeaders() {
    return this.headers;
  }

  public String getHeader(String header) {
    List<String> headers = getHeaders().get(header);
    return headers == null || headers.isEmpty() ? null : headers.get(0);
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setHeaders(HashMap<String, List<String>> headers) {
    this.headers = headers;
  }

  public void addHeader(String name, Object value) {
    List<String> list = headers.get(name);
    if(list == null) list = new ArrayList<>();
    list.add(value.toString());
    headers.put(name, list);
  }

  @Override
  public String toString() {
    String template = "Path=%s\nBody=%s\nHeaders=%s\nCookies=%s";
    return String.format(template, path, body, stringifyHeaders(), stringifyCookies());
  }

  protected String stringifyHeaders() {
    if(headers.isEmpty()) return "{}";
    return Json.stringify(headers);
  }

  private String stringifyCookies() {
    if(cookies.isEmpty()) return "[]";
    StringJoiner joiner = new StringJoiner(", ", "[", "]");
    cookies.forEach((k, v) -> joiner.add(v.toString()));
    return joiner.toString();
  }

  public static class Cookie {
    private HashMap<String, String> data;
    public static Cookie create(String cookieString) {
      HashMap<String, String> data = parse(cookieString);
      return new Cookie(data);
    }

    public static Cookie create(String name, String value) {
      HashMap<String, String> data = new HashMap<>();
      data.put("name", name);
      data.put("value", value);
      return new Cookie(data);
    }

    /**
     * Create a cookie from typical browser Cookie header strings.
     * @param cookieString A browser Cookie header string
     * @return a map of cookie properties and values
     *
     * Cookie strings are typically in the following formats:
     * - admin=true; Max-Age=3000; Expires=Fri, 3 Aug 2018 17:22:32 GMT; Path=/
     * - admin=true; foo=bar
     */
    public static HashMap<String, String> parse(String cookieString) {
      HashMap<String, String> data = new HashMap<>();
      List<String> reservedWords = Arrays.asList("max-age", "path", "expires");
      List<String> tuples = Arrays.asList(cookieString.split("; "));
      tuples.forEach(tuple -> {
        String[] pair = tuple.split("=");
        String key = pair[0].toLowerCase();
        String value = pair[1].toLowerCase();
        if(!reservedWords.contains(key)) {
          data.put("name", key);
          data.put("value", value);
        } else data.put(key, value);
      });
      return data;
    }

    private Cookie(HashMap<String, String> cookieData) {
      this.data = cookieData;
    }

    public String name() {
      return data.get("name");
    }

    public String value() {
      return data.get("value");
    }

    public String path() {
      return data.get("path");
    }

    public Long maxAge() {
      String age = data.get("max-age");
      return age != null ? Long.parseLong(age) : 0;
    }

    @Override
    public String toString() {
      return String.format("{name: %s, value: %s, path: %s, max-age: %d}", name(), value(), path(), maxAge());
    }
  }
}
