package com.boltframework.test;

import com.boltframework.BoltApplication;
import com.boltframework.config.ServerConfiguration;
import com.boltframework.utils.httpclient.HttpClient;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import com.boltframework.test.runners.BoltTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings({"unchecked", "WeakerAccess"})
@RunWith(BoltTestRunner.class)
public abstract class BoltApplicationTest<T extends BoltApplication> {

  protected static HttpClient client = new HttpClient();
  private static BoltApplication application;
  private static Logger logger = LoggerFactory.getLogger(BoltApplicationTest.class.getCanonicalName());
  private static boolean applicationStarted = false;

  @BeforeClass
  public static void startApplication() {
    Assert.assertNotNull(application);
    if(applicationStarted) return;
    logger.info("Starting app...");
    try {
      application.start((started) -> {
        if (started) {
          ServerConfiguration.HttpConfiguration httpConfig = application.getServerConfiguration().getHttpConfiguration();
          client.setUrl(httpConfig.getEndpointUrl());
          client.setReadyState(HttpClient.ReadyState.Ready);
          applicationStarted = true;
        } else client.setReadyState(HttpClient.ReadyState.Error);
      });
      while (client.getReadyState() == HttpClient.ReadyState.Idle) {
        System.out.println(".");
        Thread.sleep(300);
      }
      Assert.assertEquals(HttpClient.ReadyState.Ready, client.getReadyState());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public T getApplication() {
    return (T) application;
  }

  //TODO: Check if the application configuration is the same, and if not restart the application so the new configuration can be picked up
  public static void setApplication(BoltApplication boltApplication) {
    application = boltApplication;
  }

  public static void stopApplication() {

  }

}
