package com.boltframework.context;

import com.boltframework.web.mvc.TemplateEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Module;
import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.BodyHandler;

//TODO: Call this something else
public interface CoreModule extends Module {
  Vertx vertx();
  TemplateEngine templateEngine();
  ObjectMapper objectMapper();
  BodyHandler bodyHandler();
}
