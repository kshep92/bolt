package com.boltframework.test

import app.Configuration
import app.MyApp
import com.boltframework.config.ContextConfiguration
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

import org.slf4j.Logger

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static com.boltframework.utils.httpclient.HttpRequest.*

@ContextConfiguration(Configuration)
public class HttpClientTest extends BoltApplicationTest<MyApp> {

  protected Logger logger = LoggerFactory.getLogger(getClass())

  @Test
  public void 'send GET request'() {
    http.createRequest(get('/')).then({
      assertEquals(200, it.status)
      assertEquals('get', it.body)
    })
  }

  @Test
  public void 'send POST request'() {
    def body = 'This is the body'
    http.createRequest(post('/post').body(body)).then({
      assertEquals(200, it.status)
      assertEquals(body, it.body)
    })
  }

  @Test
  public void 'send PUT request'() {
    http.createRequest(put('/put')).then({
      assertEquals(200, it.status)
      assertEquals('put', it.body)
    })
  }

  @Test
  public void 'send DELETE request'() {
    http.createRequest(delete('/delete')).then({
      assertEquals(200, it.status)
      assertEquals('delete', it.body)
    })
  }

  @Test
  public void 'get cookies'() {
    http.createRequest(get('/cookie')).then({
      assertEquals(200, it.status)
      assertNotNull(it.cookies.get("foo"))
      assertEquals('bar', it.cookies.get('foo').value())
    })
  }

  @Test
  public void 'send a cookie with a request'() {
    def value = "hello"
    http.createRequest(post('/cookie').cookie('foo', value))
    .then({
      assertEquals(200, it.status)
      assertEquals(value, it.body)
    })
  }
}