package com.boltframework.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.vertx.core.Vertx;

/**
 * The base configuration module
 */
public class DefaultConfigurationModule extends AbstractModule {
  private Vertx vertx;

  @Provides
  public Vertx getVertx() {
    return vertx;
  }

  public void setVertx(Vertx vertx) {
    this.vertx = vertx;
  }
}
