package com.boltframework;

import com.boltframework.config.RouteRegistry;
import com.boltframework.config.ServerConfiguration;
import com.boltframework.web.WebModule;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public abstract class BoltApplication implements WebModule {

  private ServerConfiguration serverConfiguration = new ServerConfiguration();
  private Vertx vertx = Vertx.vertx();
  private HttpServer server;
  private HttpServerOptions serverOptions = new HttpServerOptions();
  private Router router;
  private PebbleEngine pebbleEngine;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private ReadyState readyState = ReadyState.Stopped;

  public ServerConfiguration getServerConfiguration() {
    return serverConfiguration;
  }

  private void bootstrap() {
    logger.info("Reading server configuration...");
    this.configure(serverConfiguration);
    serverOptions
        .setPort(serverConfiguration.getHttpConfiguration().getPort())
        .setHost(serverConfiguration.getHttpConfiguration().getHost());
    server = vertx.createHttpServer(serverOptions);
    //TODO: Make this configurable
    pebbleEngine = new PebbleEngine.Builder().loader(new FileLoader()).build();
    buildRoutes();
  }

  private void buildRoutes() {
    logger.info("Building routes...");
    router = Router.router(vertx);
    BodyHandler bodyHandler = BodyHandler.create();
    router.route().handler(CookieHandler.create());
    router.post().handler(bodyHandler);
    router.put().handler(bodyHandler);
    router.patch().handler(bodyHandler);
    RouteRegistry registry = new RouteRegistry(router, vertx, pebbleEngine, Json.mapper);
    this.doRoutes(registry);
    configureStaticAssets();
  }

  private void configureStaticAssets() {
    logger.info("Configuring static assets....");
    StaticHandler staticHandler = StaticHandler.create();
    ServerConfiguration.StaticFilesConfiguration config = serverConfiguration.getStaticFilesConfiguration();
    staticHandler.setWebRoot(config.getDir()).setMaxAgeSeconds(config.getMaxAge()).setCachingEnabled(config.isCacheEnabled());
    router.get(config.getUrl()).handler(staticHandler);
  }

  public void start() {
    start(null);
  }

  public void start(Consumer<Boolean> successCallback) {
    bootstrap();
    server.requestHandler(router::accept).listen(async -> {
      if(async.succeeded()) {
        logger.info("Listening on {}:{}...", serverOptions.getHost(), serverOptions.getPort());
        readyState = ReadyState.Running;
      }
      else
      {
        logger.error("Could not start app: " + async.cause().getMessage());
        readyState = ReadyState.Error;
      }
      if(successCallback != null) successCallback.accept(async.succeeded());

    });
  }

  public void stop() {
    stop(null);
  }

  public void stop(Consumer<Boolean> callBack) {
    server.close(async -> {
      logger.info("Closing app...");
      readyState = ReadyState.Stopped;
      int status = 0;
      if(async.succeeded()) {
        logger.info("Bye!");
        if(callBack != null) callBack.accept(async.succeeded());
      }
      else {
        logger.error("Uh oh... " + async.cause().getMessage());
        if(callBack != null) callBack.accept(async.succeeded());
        status = 500;
      }
      System.exit(status);
    });
  }

  public ReadyState getReadyState() {
    return readyState;
  }

  public enum ReadyState {
    Stopped, Running, Error
  }
}
