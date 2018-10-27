package com.boltframework.web.routing.annotations;


import io.vertx.core.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
  String[] value() default {"/"};
  String[] pattern() default {};
  String[] consumes() default {};
  String[] produces() default {};
  HttpMethod method() default HttpMethod.GET;
  boolean blocking() default false;
  int order() default 0;
}
