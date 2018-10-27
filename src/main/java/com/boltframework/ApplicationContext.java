package com.boltframework;

import com.boltframework.web.routing.annotations.RequestMapping;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that basically serves as the dependency container for a Bolt application.
 * Not intended to be used by end users directly.
 */
public class ApplicationContext {

  private static Injector injector;

  /**
   * A Map for storing managed singletons - mainly controllers for now.
   */
  private static Map<String, Object> managedSingletons = new HashMap<>();

  public static <T> T getBean(@Nonnull Class<T> beanClass) {
    if(beanClass.getAnnotation(RequestMapping.class) != null)
      return beanClass.cast(getController(beanClass));
    return injector.getInstance(beanClass);
  }

  static void initializeWith(Iterable<? extends Module> modules) {
    injector = Guice.createInjector(modules);
  }

  /**
   * Add a managed singleton to the application context with an already configured instance.
   * @param type The class of the singleton
   * @param instance A preconfigured instance of the type specified
   */
  public static void put(Class type, Object instance) {
    managedSingletons.put(type.getName(), instance);
  }

  /**
   * Add a managed singleton to the application context
   * @param type The class of the singleton
   */
  public static void put(Class<?> type) {
    managedSingletons.put(type.getName(), injector.getInstance(type));
  }

  public static <T> T getController(Class<T> controllerClass) {
    String className = controllerClass.getName();
    if(managedSingletons.get(className) == null) {
      T instance = injector.getInstance(controllerClass);
      managedSingletons.put(className, instance);
      return instance;
    }
    return controllerClass.cast(managedSingletons.get(className));
  }
}
