package com.boltframework.context;

import com.boltframework.web.mvc.FreemarkerTemplateEngine;
import com.boltframework.web.mvc.TemplateEngine;
import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;

/**
 * A concrete class for the basic dependencies each application needs.
 * Used in the event the user does not supply their own DependencyModule.
 */
public class CoreDependencies extends AbstractModule implements DependencyModule {

  @Override
  protected void configure() {
    bind(TemplateEngine.class).toInstance(templateEngine());
    bind(Vertx.class).toInstance(vertx());
  }

  public Vertx vertx() {
    return Vertx.vertx();
  }

  public TemplateEngine templateEngine() {
    return new FreemarkerTemplateEngine();
  }

}
