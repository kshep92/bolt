package com.boltframework.web.routing;

import com.boltframework.ApplicationContext;
import com.boltframework.web.HttpContext;
import com.boltframework.web.mvc.Controller;
import com.boltframework.web.routing.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ControllerCollection {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private List<String> annotations = Arrays.asList(
      Delete.class.getName(),
      Get.class.getName(),
      Head.class.getName(),
      Patch.class.getName(),
      Post.class.getName(),
      Put.class.getName(),
      Route.class.getName(),
      Trace.class.getName()
  );

  public <T extends Controller> ControllerCollection register(Class<T> controllerClass) {
    return register(controllerClass, false);
  }

  /**
   * Extract all the routing information from a controller and (optionally) add an instance of the controller
   * to the Application Context.
   * @param controllerClass A class extending {@link Controller}
   * @param lazyLoad A flag to indicate whether or not to instantiate the controller immediately.
   *                 This value defaults to true, but classes like {@link com.boltframework.test.TestApplicationServer}
   *                 find it useful to take care of instantiating the controller on its own.
   * @param <T> A generic type that extends {@link Controller}
   * @return An instance of {@link ControllerCollection} to facilitate fluent builder pattern.
   */
  @SuppressWarnings("unchecked")
  public <T extends Controller> ControllerCollection register(Class<T> controllerClass, Boolean lazyLoad) {
    RequestMapping requestMappingAnnotation = controllerClass.getAnnotation(RequestMapping.class);
    if(requestMappingAnnotation == null) {
      logger.error("Controller {} requires a @RequestMapping annotation.", controllerClass.getName());
      return this;
    }

    if(!lazyLoad) ApplicationContext.put(controllerClass);

    List<Method> methods = Arrays.stream(controllerClass.getDeclaredMethods())
        .filter(method -> getRouteAnnotation(method) != null)
        .sorted((Method a, Method b) -> {
          try {
            Annotation aRoute, bRoute;
            aRoute = getRouteAnnotation(a);
            bRoute = getRouteAnnotation(b);
            int aRouteOrder = (Integer) aRoute.annotationType().getMethod("order").invoke(aRoute);
            int bRouteOrder = (Integer) bRoute.annotationType().getMethod("order").invoke(bRoute);
            if(aRouteOrder == bRouteOrder) return 1;
            return aRouteOrder > bRouteOrder ? 1 : -1;
          } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e ) {
            return 1;
          }
        })
        .collect(Collectors.toList());
    methods.forEach(method -> {
      Annotation route = getRouteAnnotation(method);
      RouteProperties properties = new RouteProperties(requestMappingAnnotation.value()).fromAnnotation(getRouteAnnotation(method));
      properties.setHandler(ctx -> {
        HttpContext context = new HttpContext().withDelegate(ctx);
        T controller = (T) ApplicationContext.getController(controllerClass).withHttpContext(context);
        assert controller != null;
        try {
          method.invoke(controller);
          Boolean blocking = (Boolean) route.annotationType().getMethod("blocking").invoke(route);
          if(blocking) controller.clearContext(); // Clear the ThreadLocal so that it can be Garbage Collected
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          e.printStackTrace();
          logger.error(e.getMessage());
        }
      });
      PropertiesRegistry.addRouteProperties(properties);
    });
    return this;
  }

  private Annotation getRouteAnnotation(Method method) {
    Optional<Annotation> optional = Arrays.stream(method.getAnnotations()).filter(annotation -> annotations.contains(annotation.annotationType().getName())).findFirst();
    return optional.orElse(null);
  }

}
