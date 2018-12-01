package com.boltframework.web.routing;

import com.boltframework.context.ApplicationContext;
import com.boltframework.web.HttpContext;
import com.boltframework.web.routing.annotations.*;
import io.vertx.core.http.HttpMethod;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.vertx.core.http.HttpMethod.*;

public class RouteProperties extends AbstractRouteProperties {
  private HttpMethod method;
  private String prefix;
  private Boolean blocking;

  HttpMethod getMethod() {
    return method;
  }

  @Deprecated
  private void fromAnnotation(Route route) {
    method = route.method();
    blocking = route.blocking();
    for(String pattern : route.pattern()) {
      addPathRegex(pattern);
    }

    for(String _path : route.value()) {
      addPath(_path);
    }

    for(String mimeType: route.consumes()) {
      getConsumes().add(mimeType);
    }

    for(String contentType: route.produces()) {
      getProduces().add(contentType);
    }
  }

  // TODO: Fill in the rest of HTTP verbs
  RouteProperties fromAnnotation(Annotation annotation) {
    Class<? extends Annotation> type = annotation.annotationType();
    Map<Class, HttpMethod> methods = new HashMap<>();
    methods.put(Get.class, GET);
    methods.put(Post.class, POST);
    methods.put(Put.class, PUT);
    methods.put(Patch.class, PATCH);
    methods.put(Delete.class, DELETE);
    HttpMethod method;
    if(type == Route.class) method = invoke("method", annotation, HttpMethod.class);
    else method = methods.get(type) != null ? methods.get(type) : GET;
    this.method = method;

    blocking = invoke("blocking", annotation, Boolean.class);

    for(String pattern : invoke("pattern", annotation, String[].class)) {
      addPathRegex(pattern);
    }

    for(String _path : invoke("value", annotation, String[].class)) {
      addPath(_path);
    }

    for(String mimeType: invoke("consumes", annotation, String[].class)) {
      getConsumes().add(mimeType);
    }

    for(String contentType: invoke("produces", annotation, String[].class)) {
      getProduces().add(contentType);
    }

    return this;
  }

  @SuppressWarnings("unchecked")
  private <T> T invoke(String methodName, Object instance, Class<T> returnType) {
    T result;
    try {
      result = (T) instance.getClass().getMethod(methodName).invoke(instance);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
      switch (returnType.getSimpleName()) {
        case "String[]":
          result = (T) new String[] {};
          break;
        case "Boolean":
          result = (T) Boolean.FALSE;
          break;
        default:
          result = (T) new Object();
          break;
      }
    }
    return result;
  }

  @Override
  protected AbstractRouteProperties addPathRegex(String regex) {
    if(regex.startsWith("^/")) regex = regex.replace("^/", "");
    prefix = prefix.isEmpty() ? "/" : prefix + "/";
    getPatterns().add(String.format("^%s%s", prefix, regex));
    return this;
  }

  @Override
  protected AbstractRouteProperties addPath(@Nonnull String path) {
    if(!path.startsWith("/")) path = "/" + path;
    if(path.equals(prefix) && prefix.equals("/")) getPaths().add(path); /* To deal with the root route - prefix: /, path: / */
    else getPaths().add(prefix + path);
    return this;
  }


  private void setPrefix(@Nonnull String prefix) {
    if(prefix.isEmpty()) prefix = "/";
    else if(!prefix.startsWith("/")) prefix = "/" + prefix;
   this.prefix = prefix.replaceAll("[\\*/]+$", ""); // Strip trailing slashes
  }

  @Deprecated
  RouteProperties(String prefix, Route routeAnnotation) {
    setPrefix(prefix);
    fromAnnotation(routeAnnotation);
  }

  RouteProperties(String prefix) {
    setPrefix(prefix);
  }

  @Deprecated
  RouteProperties(String path, HttpMethod method) {
    this.method = method;
    addPath(path);
  }

  public RouteProperties handler(Consumer<HttpContext> handler) {
    setHandler(ctx -> handler.accept(ApplicationContext.getBean(HttpContext.class).withDelegate(ctx)));
    return this;
  }

  boolean isBlocking() {
    return blocking;
  }
}
