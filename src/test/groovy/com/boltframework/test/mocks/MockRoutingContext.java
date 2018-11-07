package com.boltframework.test.mocks;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockRoutingContext implements RoutingContext {
  @Override
  public HttpServerRequest request() {
    return null;
  }

  @Override
  public HttpServerResponse response() {
    return null;
  }

  @Override
  public void next() {

  }

  @Override
  public void fail(int statusCode) {

  }

  @Override
  public void fail(Throwable throwable) {

  }

  @Override
  public RoutingContext put(String key, Object obj) {
    return null;
  }

  @Override
  public <T> T get(String key) {
    return null;
  }

  @Override
  public <T> T remove(String key) {
    return null;
  }

  @Override
  public Map<String, Object> data() {
    return null;
  }

  @Override
  public Vertx vertx() {
    return null;
  }

  @Override
  public String mountPoint() {
    return null;
  }

  @Override
  public Route currentRoute() {
    return null;
  }

  @Override
  public String normalisedPath() {
    return null;
  }

  @Override
  public Cookie getCookie(String name) {
    return null;
  }

  @Override
  public RoutingContext addCookie(Cookie cookie) {
    return null;
  }

  @Override
  public Cookie removeCookie(String name, boolean invalidate) {
    return null;
  }

  @Override
  public int cookieCount() {
    return 0;
  }

  @Override
  public Set<Cookie> cookies() {
    return null;
  }

  @Override
  public String getBodyAsString() {
    return null;
  }

  @Override
  public String getBodyAsString(String encoding) {
    return null;
  }

  @Override
  public JsonObject getBodyAsJson() {
    return null;
  }

  @Override
  public JsonArray getBodyAsJsonArray() {
    return null;
  }

  @Override
  public Buffer getBody() {
    return null;
  }

  @Override
  public Set<FileUpload> fileUploads() {
    return null;
  }

  @Override
  public Session session() {
    return null;
  }

  @Override
  public User user() {
    return null;
  }

  @Override
  public Throwable failure() {
    return null;
  }

  @Override
  public int statusCode() {
    return 0;
  }

  @Override
  public String getAcceptableContentType() {
    return null;
  }

  @Override
  public ParsedHeaderValues parsedHeaders() {
    return null;
  }

  @Override
  public int addHeadersEndHandler(Handler<Void> handler) {
    return 0;
  }

  @Override
  public boolean removeHeadersEndHandler(int handlerID) {
    return false;
  }

  @Override
  public int addBodyEndHandler(Handler<Void> handler) {
    return 0;
  }

  @Override
  public boolean removeBodyEndHandler(int handlerID) {
    return false;
  }

  @Override
  public boolean failed() {
    return false;
  }

  @Override
  public void setBody(Buffer body) {

  }

  @Override
  public void setSession(Session session) {

  }

  @Override
  public void setUser(User user) {

  }

  @Override
  public void clearUser() {

  }

  @Override
  public void setAcceptableContentType(String contentType) {

  }

  @Override
  public void reroute(HttpMethod method, String path) {

  }

  @Override
  public List<Locale> acceptableLocales() {
    return null;
  }

  @Override
  public Map<String, String> pathParams() {
    return null;
  }

  @Override
  public String pathParam(String name) {
    return null;
  }

  @Override
  public MultiMap queryParams() {
    return null;
  }

  @Override
  public List<String> queryParam(String query) {
    return null;
  }

  @Override
  public Cookie removeCookie(String name) {
    return null;
  }

  @Override
  public void reroute(String path) {

  }

  @Override
  public List<LanguageHeader> acceptableLanguages() {
    return null;
  }

  @Override
  public Locale preferredLocale() {
    return null;
  }

  @Override
  public LanguageHeader preferredLanguage() {
    return null;
  }
}
