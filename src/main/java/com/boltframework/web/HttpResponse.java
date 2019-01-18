package com.boltframework.web;

import com.boltframework.web.mvc.TemplateEngine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;

import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.*;

public class HttpResponse {

  private HttpServerResponse response;
  private ObjectMapper mapper;
  private TemplateEngine templateEngine;
  private Map<String, Object> contextData;

  @Inject
  public HttpResponse(ObjectMapper mapper, TemplateEngine templateEngine) {
    this.mapper = mapper;
    this.templateEngine = templateEngine;
  }

  HttpResponse withDelegate(HttpServerResponse delegate) {
    this.response = delegate;
    return this;
  }

  HttpResponse withContext(Map<String, Object> contextData) {
    this.contextData = contextData;
    return this;
  }

  public HttpResponse addHeader(String name, String value) {
    response.putHeader(name, value);
    return this;
  }


  public HttpResponse addHeader(CharSequence name, String value) {
    response.putHeader(name, value);
    return this;
  }

  public HttpResponse badRequest() {
    return setStatusCode(BAD_REQUEST);
  }

  public void badRequest(String body) {
    badRequest().send(body);
  }

  public HttpResponse error() {
    return setStatusCode(INTERNAL_SERVER_ERROR);
  }

  public void error(String body) {
    error().send(body);
  }

  public void end() {
    response.end();
  }

  public HttpResponse forbidden() {
    return setStatusCode(FORBIDDEN);
  }

  public void forbidden(String body) {
    forbidden().send(body);
  }

  public int getStatusCode() {
    return response.getStatusCode();
  }

  public void html(String htmlString) {
    html().send(htmlString);
  }

  public HttpResponse html() {
    return setContentType("text/html");
  }

  public void json(Object entity) {
    try {
      json().send(mapper.writeValueAsString(entity));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void json(String json) {
    json().send(json);
  }

  public HttpResponse json() {
    return setContentType("application/json");
  }

  public void notFound(String body) {
    notFound().send(body);
  }

  public HttpResponse notFound() {
    return setStatusCode(NOT_FOUND);
  }

  public HttpResponse ok() {
    return setStatusCode(OK);
  }

  public void ok(String body) {
    ok().send(body);
  }

  public void redirect(String path) {
    redirect(formatPath(path), 303);
  }

  private String formatPath(String path) {
    return path.matches("^(https?://|/).*") ? path : "/" + path;
  }

  public void redirect(String path, Integer code) {
    response.setStatusCode(code).putHeader("Location", formatPath(path)).end();
  }

  public void render(String templateName) {
    send(templateEngine.render(templateName, contextData));
  }

  public void renderHtml(String templateName) {
    html().render(templateName);
  }

  public void send(String body) {
    response.end(body);
  }

  public void todo() {
    setStatusCode(NOT_IMPLEMENTED).html("<h1 style=\"font-family: Arial Black, sans-serif\">TODO: Implement this route.</h1>");
  }

  public void todo(String message) {
    setStatusCode(NOT_IMPLEMENTED).html(message);
  }

  public HttpResponse unauthorized() {
    return setStatusCode(UNAUTHORIZED);
  }

  public void unauthorized(String message) {
    unauthorized().send(message);
  }

  public HttpResponse write(String chunk) {
    response.write(chunk);
    return this;
  }

  public HttpResponse writeLine(String chunk) {
    return write(chunk + "\n");
  }

  public void sendFile(String fileName) {
    response.sendFile(fileName);
  }

  public HttpResponse setChunked(Boolean chunked) {
    response.setChunked(chunked);
    return this;
  }

  public HttpResponse setContentType(String contentType) {
    response.putHeader("Content-Type", contentType);
    return this;
  }

  public HttpResponse setStatusCode(Integer status) {
    response.setStatusCode(status);
    return this;
  }

  public HttpResponse setStatusCode(HttpResponseStatus status) {
    return setStatusCode(status.code());
  }
}
