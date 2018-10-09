package com.boltframework.old;

import com.google.inject.AbstractModule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ContextConfiguration {
  Class<? extends AbstractModule>[] value();
}
