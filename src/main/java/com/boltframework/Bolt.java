package com.boltframework;

import com.boltframework.config.ContextConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"WeakerAccess"})
public abstract class Bolt {

  private static BoltApplication instance;

  public static BoltApplication getInstance() {
    return instance;
  }

  /* For custom bahaviour: Bolt.getInstance(MyApp.class).start( ... ) */
  public static <T extends BoltApplication> T getInstance(Class<T> instanceType) {
    Set<? extends AbstractModule> modules = getConfigurationModules(instanceType);
    return Guice.createInjector(modules).getInstance(instanceType);
  }

  /* The preferred way: Bolt.run(MyApp.class) */
  public static <T extends BoltApplication> void run(Class<T> applicationClass) {
    instance = getInstance(applicationClass);
    instance.beforeStart();
    instance.start(started -> {
      if(started) instance.onStart();
    });

  }

  private static Set<AbstractModule> getConfigurationModules(Class clazz) {
    HashSet<AbstractModule> result = new HashSet<>();
    ContextConfiguration contextConfiguration = (ContextConfiguration) clazz.getAnnotation(ContextConfiguration.class);
    if(contextConfiguration == null) return result;
    Arrays.asList(contextConfiguration.value()).forEach(_class -> {
      try {
        result.add(_class.newInstance());
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    });
    return result;
  }
}
