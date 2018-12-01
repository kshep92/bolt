package com.boltframework;

import com.boltframework.context.ApplicationContext;
import com.boltframework.context.CoreDependencies;
import com.boltframework.context.CoreModule;
import com.boltframework.data.ConverterRegistry;
import com.boltframework.data.converters.*;
import com.boltframework.utils.Env;
import com.boltframework.utils.httpclient.MethodOverrideHandler;
import com.boltframework.web.WebService;
import com.boltframework.web.mvc.TemplateEngine;
import com.boltframework.web.routing.PropertiesRegistry;
import com.boltframework.web.routing.InterceptorBuilder;
import com.boltframework.web.routing.InterceptorCollection;
import com.boltframework.web.routing.InterceptorProperties;
import com.boltframework.web.routing.ResourceHandlerBuilder;
import com.boltframework.web.routing.ResourceHandlerCollection;
import com.boltframework.web.routing.ResourceHandlerProperties;
import com.boltframework.web.routing.RouteBuilder;
import com.boltframework.web.routing.ControllerCollection;
import com.boltframework.web.routing.RouteProperties;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static io.vertx.core.http.HttpMethod.POST;

public class Bolt {

  protected static Logger logger = LoggerFactory.getLogger(Bolt.class);
  private WebService webService;
  private Class<? extends WebService> serviceClass;
  private CoreModule coreModule;
  private ReadyState readyState;
  private Vertx vertx;
  private Router router;
  private HttpServer server;

  protected Bolt() {}

  protected Router getRouter() {
    return router;
  }

  protected void setRouter(Router router) {
    this.router = router;
  }

  protected Class<? extends WebService> getServiceClass() {
    return serviceClass;
  }

  protected void setServiceClass(Class<? extends WebService> serviceClass) {
    this.serviceClass = serviceClass;
  }

  protected Bolt(Class<? extends WebService> webServiceClass) {
    this.serviceClass = webServiceClass;
    readyState = ReadyState.Stopped;
  }

  public void start() {
    @SuppressWarnings("ConstantConditions")
    Integer port = Env.getInt("http.port", 3000);
    start(port, (started) -> {});
  }

  public void start(int port) {
    start(port, (started) -> {});
  }

  public void start(int port, Consumer<Boolean> callback) {
    buildApplicationContext();
    registerTypeConverters();
    buildRoutes();
    server = vertx.createHttpServer();
    server.requestHandler(router::accept).listen(port, async -> {
      if(async.succeeded()) {
        logger.info("Listening on http://localhost:{}", port);
        readyState = ReadyState.Running;
      } else {
        logger.error("Could not start app: " + async.cause().getMessage());
        readyState = ReadyState.Error;
      }
      callback.accept(async.succeeded());
    });
  }

  private void registerTypeConverters() {
    ConverterRegistry.add(Boolean.class, new BooleanConverter());
    ConverterRegistry.add(Double.class, new DoubleConverter());
    ConverterRegistry.add(Integer.class, new IntConverter());
    ConverterRegistry.add(Long.class, new LongConverter());
    ConverterRegistry.add(String.class, new StringConverter());
  }

  public void stop(Runnable runnable) {
    server.close((result) -> {
      if(result.succeeded()) logger.info("Terminating web service. Bye!");
      else logger.error("Could not gracefully terminate application.");
      readyState = ReadyState.Stopped;
      runnable.run();
    });
  }

  public void stop() {
    stop(() -> {});
  }

  public ReadyState getReadyState() {
    return readyState;
  }

  protected void buildApplicationContext() {
    if(coreModule == null) coreModule = new CoreDependencies();
    ApplicationContext.initializeWith(coreModule);
    ApplicationContext.getBean(TemplateEngine.class).build();
    vertx = ApplicationContext.getBean(Vertx.class);
    router = Router.router(vertx);
  }

  //TODO: Use classpath scanning to find all controllers
  private void buildRoutes() {
    webService = ApplicationContext.getBean(serviceClass);
    logger.info("Building routes...");
    BodyHandler bodyHandler = coreModule.bodyHandler();
    Boolean useHttpMethodOverride = Env.getBoolean("http.use-overrides");
    if(useHttpMethodOverride) {
      logger.info("[http.use-overrides] Using HTTP method overrides! Slight performance hit ahead.");
      router.post().handler(new MethodOverrideHandler("PATCH"));
      router.get().handler(new MethodOverrideHandler("CONNECT"));
    }
    router.route().handler(CookieHandler.create());
    router.post().handler(bodyHandler);
    router.put().handler(bodyHandler);
    router.patch().handler(bodyHandler);
    addInterceptors();
    addHttpEndpoints();
    addStaticResourceHandlers();
  }

  private void addInterceptors() {
    logger.info("Adding interceptors...");
    InterceptorCollection registry = new InterceptorCollection();
    webService.addInterceptors(registry);
    for(InterceptorProperties properties : PropertiesRegistry.getInterceptorProperties()) {
      InterceptorBuilder builder = new InterceptorBuilder(properties);
      builder.buildRoutes(router);
    }
  }

  //TODO: Experiment with Classpath scanning to gather the controllers
  protected void addHttpEndpoints() {
    logger.info("Creating HTTP actions...");
    ControllerCollection registry = new ControllerCollection();
    webService.addControllers(registry);
    for(RouteProperties properties : PropertiesRegistry.getRouteProperties()) {
      RouteBuilder builder = new RouteBuilder(properties);
      builder.buildRoutes(router);
    }
  }

  private void addStaticResourceHandlers() {
    logger.debug("Adding static resource handlers...");
    ResourceHandlerCollection registry = new ResourceHandlerCollection();
    webService.addResourceHandlers(registry);
    for(ResourceHandlerProperties properties : PropertiesRegistry.getResourceHandlerProperties()) {
      ResourceHandlerBuilder builder = new ResourceHandlerBuilder(properties);
      builder.buildRoutes(router);
    }
  }

  public static Bolt createService(Class<? extends WebService> webServiceClass) {
    return new Bolt(webServiceClass);
  }

  public Bolt withContext(CoreModule coreModule) {
    this.coreModule = coreModule;
    return this;
  }

}
