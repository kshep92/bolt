package com.boltframework.test;

import com.boltframework.context.ApplicationContext;
import com.boltframework.Bolt;
import com.boltframework.ReadyState;
import com.boltframework.context.CoreModule;
import com.boltframework.utils.httpclient.HttpClient;
import com.boltframework.utils.httpclient.HttpRequest;
import com.boltframework.web.WebService;
import com.boltframework.web.mvc.Controller;
import com.boltframework.web.routing.ControllerCollection;
import com.boltframework.web.routing.PropertiesRegistry;
import com.boltframework.web.routing.RouteBuilder;
import com.boltframework.web.routing.RouteProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class TestApplicationServer extends Bolt {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private HttpClient httpClient = new HttpClient();
  private Controller controller;
  private int port = new Random().nextInt(3000) + 6000;
  private boolean contextBuilt = false;

  public TestApplicationServer(){}

  public TestApplicationServer(Class<? extends WebService> webServiceClass) {
    super(webServiceClass);
  }

  public TestApplicationServer(Controller controller) {
    this.controller = controller;
  }

  @Override
  protected void addHttpEndpoints() {
    logger.info("Creating HTTP actions...");
    ControllerCollection registry = new ControllerCollection();
    registry.register(controller.getClass(), true);
    ApplicationContext.put(controller.getClass(), controller);
    for(RouteProperties properties : PropertiesRegistry.getRouteProperties()) {
      RouteBuilder builder = new RouteBuilder(properties);
      builder.buildRoutes(getRouter());
    }
  }

  public HttpClient createRequest(HttpRequest request) {
    if (getReadyState() != ReadyState.Running) {
      start();
      if(getReadyState() == ReadyState.Error) return null;
      else httpClient.setUrl("http://localhost:" + port);
    }
    return httpClient.createRequest(request);
  }

  public void setController(Controller controller) {
    if(this.controller != null) {
      ApplicationContext.put(controller.getClass(), controller);
      return;
    }
    this.controller = controller;
  }

  public <T> T getBean(Class<T> type) {
    return ApplicationContext.getBean(type);
  }

  @Override
  public TestApplicationServer withContext(CoreModule dependencies) {
    if(contextBuilt) {
      logger.error("Context has already been built.");
      return this;
    }
    super.withContext(dependencies);
    // Building the application context here since we might need to getBean beans before the server is started.
    buildApplicationContext();
    contextBuilt = true;
    return this;
  }

  public TestApplicationServer withMvcConfiguration(Class<? extends WebService> serviceClass) {
    setServiceClass(serviceClass);
    return this;
  }

  public TestApplicationServer forController(Controller controller) {
    setController(controller);
    return this;
  }

  @Override
  protected void buildApplicationContext() {
    if(!contextBuilt) super.buildApplicationContext();
  }

  @Override
  public void start() {
    if (getServiceClass() == null) setServiceClass(DefaultWebService.class);
    assert controller != null;
    start(port);
    while (getReadyState() != ReadyState.Running) {
      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }


  /*@SuppressWarnings("unchecked")
  private Class<T> getControllerClass() {
    return (Class<T>) ((ParameterizedType) getClass().getTypeParameters()[0]).getRawType();
  }*/

  // If the user doesn't pass in their web service, we'll use an empty one with no interceptors, static handlers, etc.
  private static class DefaultWebService extends WebService {}
}
