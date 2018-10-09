package com.boltframework.test

import app.TestApplication
import com.boltframework.test.rules.TestServer
import org.junit.ClassRule
import org.junit.Test
import org.slf4j.LoggerFactory

import org.slf4j.Logger

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static com.boltframework.utils.httpclient.HttpRequest.*

public class ApplicationControllerTest {

  protected Logger logger = LoggerFactory.getLogger(getClass())

  @ClassRule
  public static TestServer server = new TestServer(TestApplication)

  @Test
  public void "server startup"() {
    logger.debug("OK")
  }

  @Test
  public void 'send GET request'() {
    server.createRequest(get('/')).then({
      assertEquals(200, it.status)
      assertEquals('index', it.body)
    })
  }

  @Test
  public void 'send POST request'() {
    def body = 'This is the body'
    server.createRequest(post('/post').body(body)).then({
      assertEquals(200, it.status)
      assertEquals(body, it.body)
    })
  }

  @Test
  public void 'send PUT request'() {
    def body = "put"
    server.createRequest(put('/put').body(body)).then({
      assertEquals(200, it.status)
      assertEquals(body, it.body)
    })
  }

  @Test
  public void 'send DELETE request'() {
    def body = "delete"
    server.createRequest(delete('/delete')).then({
      assertEquals(200, it.status)
      assertEquals(body, it.body)
    })
  }

  @Test
  public void 'get cookies'() {
    server.createRequest(get('/cookie')).then({
      assertEquals(200, it.status)
      assertNotNull(it.cookies.get("foo"))
      assertEquals('bar', it.cookies.get('foo').value())
    })
  }

  @Test
  public void 'send a cookie with a request'() {
    def value = "hello"
    server.createRequest(post('/cookie').cookie('foo', value))
    .then({
      assertEquals(200, it.status)
      assertEquals(value, it.body)
    })
  }
}