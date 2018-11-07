package com.boltframework.web;

import com.boltframework.data.Converter;
import com.boltframework.data.ConverterRegistry;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

  private HttpServerRequest request;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public HttpRequest(HttpServerRequest request) {
    this.request = request;
  }

  public HttpRequest withDelegate(HttpServerRequest httpServerRequest) {
    request = httpServerRequest;
    return this;
  }

  public Boolean accepts(String contentType) {
    return request.getHeader("Accept").matches(convertToRegex(contentType));
  }

  public Boolean contentTypeMatches(String contentType) {
    return request.getHeader("Content-Type").matches(convertToRegex(contentType));
  }

  public String getAbsoluteUrl() {
    return request.absoluteURI();
  }

  public String getHeader(String name) {
    return request.getHeader(name);
  }

  public MultiMap getHeaders() {
    return request.headers();
  }

  public String getHost() {
    return request.host();
  }

  public String getIpAddress() {
    return request.remoteAddress().host();
  }

  public HttpMethod getMethod() {
    return request.method();
  }

  public MultiMap getParams() {
    return request.params();
  }

  public String getPath() {
    return request.path();
  }

  public String getParam(String name) {
    return getParams().get(name);
  }

  public <T> T getParam(String name, Class<T> type) {
    Converter<T> converter = ConverterRegistry.getConverter(type);
    if(converter == null) {
      logger.error("No converter found for class {}", type);
      return null;
    }
    return converter.convert(getParam(name));
  }

  public Integer getPort() {
    return request.remoteAddress().port();
  }

  public Map<String, String> getQuery() {
    HashMap<String, String> params = new HashMap<>();
    String queryString = request.query();
    if (queryString == null || queryString.isEmpty()) return params;
    List<String> queryList = Arrays.asList(queryString.split("&"));
    queryList.forEach(pair -> {
      String[] parts = pair.split("=");
      if (parts.length > 1)
        try {
          params.put(parts[0], URLDecoder.decode(parts[1], "utf-8"));
        } catch (UnsupportedEncodingException e) {
          params.put(parts[0], null);
        }
      else
        params.put(parts[0], null);
    });
    return params;
  }

  public String getQueryParam(String param) {
    return getQuery().get(param);
  }

  public <T> T getQueryParam(String param, Class<T> type) {
    Converter<T> converter = ConverterRegistry.getConverter(type);
    if(converter == null) {
      logger.error("No converter found for class {}", type);
      return null;
    }
    return converter.convert(getQueryParam(param));
  }

  public String getUserAgent() {
    return request.getHeader("User-Agent");
  }

  public Boolean isSecure() {
    return request.isSSL();
  }

  private String convertToRegex(String glob) {
    if(glob.charAt(0) == '*')
      glob = glob.substring(1);
    if(glob.charAt(glob.length() - 1) == '*')
      glob = glob.substring(0, glob.length() - 1);
    return ("*" + glob + "*").replace("*", ".*");
  }
}
