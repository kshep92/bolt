package com.boltframework.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import static io.netty.handler.codec.http.HttpResponseStatus.*;

public class HttpResponse {

  private HttpServerResponse response;
  private ObjectMapper mapper;

  HttpResponse(HttpServerResponse response,
               ObjectMapper mapper) {
    this.response = response;
    this.mapper = mapper;
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

  public void html(String htmlString) {
    setContentType("text/html").send(htmlString);
  }

  public void json(Object entity) {
    try {
      setContentType("application/json").send(mapper.writeValueAsString(entity));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void json(String json) {
    setContentType("application/json").send(json);
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
    path = path.matches("^(https?://|/).*") ? path : "/" + path;
    redirect(path, 303);
  }

  public void redirect(String path, Integer code) {
    response.setStatusCode(code).putHeader("Location", path).end();
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
