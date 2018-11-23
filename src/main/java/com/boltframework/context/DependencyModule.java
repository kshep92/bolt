package com.boltframework.context;

import com.boltframework.web.mvc.TemplateEngine;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.vertx.core.Vertx;

public interface DependencyModule extends Module {
  Vertx getVertx();
  void setVertx(Vertx vertx);
  /*TemplateEngine templateEngine();
  void setTemplateEngine(TemplateEngine templateEngine);*/
}
