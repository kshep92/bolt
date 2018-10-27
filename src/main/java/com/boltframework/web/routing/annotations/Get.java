package com.boltframework.web.routing.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Get {
  String[] value() default {"/"};
  String[] pattern() default {};
  String[] consumes() default {};
  String[] produces() default {};
  boolean blocking() default false;
  int order() default 0;
}
