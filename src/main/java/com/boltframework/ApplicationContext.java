package com.boltframework;

import com.boltframework.web.routing.annotations.Path;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

  private static Injector injector;

  private static Map<String, Object> controllers = new HashMap<>();

  public static <T> T getBean(Class<T> beanClass) {
    if(beanClass.getAnnotation(Path.class) != null)
      return getController(beanClass);
    return injector.getInstance(beanClass);
  }

  public static void initialize(Iterable<? extends Module> modules) {
    injector = Guice.createInjector(modules);
  }

  @SuppressWarnings("unchecked")
  public static void addController(Class controllerClass) {
    controllers.put(controllerClass.getName(), injector.getInstance(controllerClass));
  }

  public static <T> T getController(Class<T> controllerClass) {
    String className = controllerClass.getName();
    if(controllers.get(className) == null) addController(controllerClass);
    return controllerClass.cast(controllers.get(className));
  }

}
