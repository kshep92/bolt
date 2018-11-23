package com.boltframework.web.routing;

import app.controllers.Application;
import com.boltframework.web.routing.annotations.Get;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerCollectionTest {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Test
  public void get() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
    Method method = Application.class.getDeclaredMethod("index");
    Annotation annotation = method.getAnnotation(Get.class);
    logger.debug("Annotation simple name is: {}, Route value is: {}", annotation.annotationType().getSimpleName(), annotation.annotationType().getMethod("value").invoke(annotation));
    logger.debug(String[].class.getSimpleName());
  }

}
