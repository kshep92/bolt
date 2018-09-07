package com.boltframework.test;

import com.boltframework.BoltApplication;
import com.boltframework.config.HttpConfiguration;
import com.boltframework.test.runners.BoltTestRunner;
import com.boltframework.utils.httpclient.HttpClient;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.boltframework.BoltApplication.ReadyState.Running;
import static com.boltframework.BoltApplication.ReadyState.Stopped;


@SuppressWarnings({"unchecked", "WeakerAccess"})
@RunWith(BoltTestRunner.class)
public abstract class BoltApplicationTest<T extends BoltApplication> {

  private static Logger logger = LoggerFactory.getLogger(BoltApplicationTest.class.getCanonicalName());
  protected static BoltApplication app;

  protected static HttpClient http = new HttpClient();

  private static void startApplication() {
    // Detect if an application is already running, if so do nothing.
    if(app.getReadyState() == Running) return;

    // Otherwise, start the application
    logger.info("Starting app...");
    try {
      app.start((started) -> {
        if (started) {
          HttpConfiguration httpConfig = app.getServerConfiguration().getHttp();
          http.setUrl(httpConfig.getEndpointUrl());
        }
      });
      while (app.getReadyState() == Stopped) {
        Thread.sleep(300);
      }
      Assert.assertEquals(Running, app.getReadyState());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static BoltApplication _getRunningApp() {
    return app;
  }

  public T getApp() {
    return (T) app;
  }

  public static void setApp(BoltApplication boltApplication) {
    app = boltApplication;
    startApplication();
  }

  private static void stopApplication() {
    try {
      if (app != null && app.getReadyState() == Running) {
        logger.info("Application is running. Attempting to shut it down...");
        app.stop();
        while (app.getReadyState() == Running) {
          Thread.sleep(300);
        }
        Assert.assertEquals(Stopped, app.getReadyState());
        logger.info("Stopped application running on port {}", app.getServerConfiguration().getHttp().getPort());
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
