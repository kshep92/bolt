package com.boltframework.test.rules;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: Make this the new way to spin up test servers
public class TestServer extends ExternalResource {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected void before() throws Throwable {
    super.before();
    logger.debug("Running tests for {}", getClass());
  }

  @Override
  protected void after() {
    super.after();
    logger.debug("Wrapping up...");
    try {
      Thread.sleep(5000);
      logger.debug("Finished tests for {}", getClass());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
