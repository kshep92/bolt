package com.boltframework.test.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.HashSet;
import java.util.Set;

public class Registry {

  private Injector injector;

  private Registry(Injector injector) {
    this.injector = injector;
  }

  public static Registry from(Class<? extends AbstractModule>... configurationClasses) {
    return new Registry(Guice.createInjector(getModules(configurationClasses)));
  }

  @SuppressWarnings("unchecked")
  private static Set<? extends Module> getModules(Class<? extends AbstractModule>[] configurationClasses) {
    Set modules = new HashSet<>();
    if(configurationClasses.length == 0) return modules;
    for(Class<? extends AbstractModule> entry: configurationClasses) {
      try {
        modules.add(entry.newInstance());
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return modules;
  }

  public <T> T getInstance(Class<T> instanceType) {
    return injector.getInstance(instanceType);
  }
}
