package com.boltframework.context;

import com.boltframework.web.mvc.FreemarkerTemplateEngine;
import com.boltframework.web.mvc.TemplateEngine;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.handler.BodyHandler;

import java.text.DateFormat;

/**
 * A concrete class for the basic dependencies each application needs.
 * Used in the event the user does not supply their own DependencyModule.
 */
public class CoreDependencies extends AbstractModule implements CoreModule {

  @Override
  protected void configure() {
    bind(TemplateEngine.class).toInstance(templateEngine());
    bind(Vertx.class).toInstance(vertx());
    bind(ObjectMapper.class).toInstance(objectMapper());
  }

  public Vertx vertx() {
    return Vertx.vertx();
  }

  public TemplateEngine templateEngine() {
    return new FreemarkerTemplateEngine();
  }

  public ObjectMapper objectMapper() {
    return Json.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDateFormat(DateFormat.getDateInstance(DateFormat.SHORT));
  }

  public BodyHandler bodyHandler() {
    return BodyHandler.create();
  }
}
