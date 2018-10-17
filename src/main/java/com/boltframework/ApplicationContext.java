package com.boltframework;

import com.boltframework.web.mvc.Controller;
import com.boltframework.web.routing.annotations.RequestMapping;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

  private static Injector injector;

  private static Map<String, Controller> controllers = new HashMap<>();

  public static <T> T getBean(@Nonnull Class<T> beanClass) {
    if(beanClass.getAnnotation(RequestMapping.class) != null)
      return beanClass.cast(getController(beanClass));
    return injector.getInstance(beanClass);
  }

  static void initializeWith(Iterable<? extends Module> modules) {
    injector = Guice.createInjector(modules);
  }

  public static void put(Class<? extends Controller> controllerClass, Controller controller) {
    controllers.put(controllerClass.getName(), controller);
  }

  public static <T> T getController(Class<T> controllerClass) {
    String className = controllerClass.getName();
    if(controllers.get(className) == null) controllers.put(className, (Controller) injector.getInstance(controllerClass));
    return controllerClass.cast(controllers.get(className));
  }
}
