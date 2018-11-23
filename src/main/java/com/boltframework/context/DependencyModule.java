package com.boltframework.context;

import com.boltframework.web.mvc.TemplateEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Module;
import io.vertx.core.Vertx;

public interface DependencyModule extends Module {
  Vertx vertx();
  TemplateEngine templateEngine();
  ObjectMapper objectMapper();
}
