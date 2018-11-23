package com.boltframework.context;

import com.boltframework.web.mvc.TemplateEngine;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.vertx.core.Vertx;

/**
 * A concrete class for the basic dependencies each application needs.
 * Used in the event the user does not supply their own DependencyModule.
 */
public class CoreDependencies extends AbstractModule implements DependencyModule {
  private Vertx vertx;

  @Provides
  public Vertx getVertx() {
    return vertx;
  }

  public void setVertx(Vertx vertx) {
    this.vertx = vertx;
  }

}
