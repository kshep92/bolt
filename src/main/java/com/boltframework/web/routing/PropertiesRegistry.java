package com.boltframework.web.routing;

import java.util.ArrayList;
import java.util.List;

public class PropertiesRegistry {
  private static List<RouteProperties> routeProperties = new ArrayList<>();
  private static List<InterceptorProperties> interceptorProperties = new ArrayList<>();
  private static List<ResourceHandlerProperties> resourceHandlerProperties = new ArrayList<>();
  private static List<ExceptionHandlerProperties> exceptionHandlerProperties = new ArrayList<>();

  public static List<InterceptorProperties> getInterceptorProperties() {
    return interceptorProperties;
  }

  public static List<RouteProperties> getRouteProperties() {
    return routeProperties;
  }

  public static List<ResourceHandlerProperties> getResourceHandlerProperties() {
    return resourceHandlerProperties;
  }

  public static List<ExceptionHandlerProperties> getExceptionHandlerProperties() {
    return exceptionHandlerProperties;
  }

  static InterceptorProperties addInterceptorProperties(InterceptorProperties properties) {
    return addAndReturnLastElement(InterceptorProperties.class, interceptorProperties, properties);
  }

  static ExceptionHandlerProperties addExceptionHandlerProperties(ExceptionHandlerProperties properties) {
    return addAndReturnLastElement(ExceptionHandlerProperties.class, exceptionHandlerProperties, properties);
  }

  static RouteProperties addRouteProperties(RouteProperties properties) {
    return addAndReturnLastElement(RouteProperties.class, routeProperties, properties);
  }

  static ResourceHandlerProperties addResourceHandlerProperties(ResourceHandlerProperties properties) {
    return addAndReturnLastElement(ResourceHandlerProperties.class, resourceHandlerProperties, properties);
  }

  private static <T> T addAndReturnLastElement(Class<T> collectionType, List<T> collection, T entry) {
    collection.add(entry);
    return collectionType.cast(collection.get(collection.size()-1));
  }

}
