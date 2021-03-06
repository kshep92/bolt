package com.boltframework.context;

import com.boltframework.web.routing.annotations.RequestMapping;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that basically serves as the dependency container for a Bolt application.
 * Not intended to be used by end users directly.
 */
public class ApplicationContext {

  private static Injector injector;
  private static Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

  /**
   * A Map for storing managed singletons - mainly controllers for now.
   */
  private static Map<String, Object> managedSingletons = new HashMap<>();

  public static <T> T getBean(@Nonnull Class<T> beanClass) {
    if(injector == null) {
      logger.warn("!!ApplicationContext not initialized!! Will attempt manual instantiation.");
      try {
        return beanClass.newInstance();
      } catch (IllegalAccessException | InstantiationException e) {
        e.printStackTrace();
        return null;
      }
    }
    if(beanClass.getAnnotation(RequestMapping.class) != null)
      return beanClass.cast(getController(beanClass));
    return injector.getInstance(beanClass);
  }

  public static void initializeWith(CoreModule coreModule) {
    injector = Guice.createInjector(coreModule);
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

  public void injectMemebers(Object instance) {
    injector.injectMembers(instance);
  }
}
