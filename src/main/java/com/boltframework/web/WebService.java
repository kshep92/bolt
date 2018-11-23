package com.boltframework.web;

import com.boltframework.context.ApplicationContext;
import com.boltframework.web.mvc.Controller;
import com.boltframework.web.routing.ControllerCollection;
import com.boltframework.web.routing.InterceptorCollection;
import com.boltframework.web.routing.ResourceHandlerCollection;
import com.boltframework.web.routing.annotations.RequestMapping;
import com.boltframework.web.routing.annotations.Route;

//TODO: Rename to ServiceConfigurer or something
public abstract class WebService {

  public <T> T require(Class<T> beanType) {
    return ApplicationContext.getBean(beanType);
  }

  public void addInterceptors(InterceptorCollection interceptors) {}
  public void addControllers(ControllerCollection controllers) {
    controllers.register(DefaultController.class);
  }
  public void addResourceHandlers(ResourceHandlerCollection handlers) {}

  @RequestMapping
  public static class DefaultController extends Controller {

    @Route
    public void index() {
      response().setStatusCode(200).send("Hello, world!");
    }

  }
}
