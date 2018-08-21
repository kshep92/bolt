package com.boltframework.test

import org.junit.Test

class HttpClientSubTest extends HttpClientTest {

  @Test
  public void "check inheritance"() {
    logger.debug("From the sub test")
  }

  @Test
  public void 'basic test'() {
    logger.debug("A basic test")
  }
}
