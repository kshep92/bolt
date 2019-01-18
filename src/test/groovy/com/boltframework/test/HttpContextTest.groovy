package com.boltframework.test

import app.TestApplication
import app.controllers.HttpContextController
import com.boltframework.data.ConverterRegistry
import com.boltframework.utils.Json
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.junit.Assert.*
import static com.boltframework.utils.httpclient.HttpRequest.*

class HttpContextTest {

  Logger logger = LoggerFactory.getLogger(getClass())
  TestApplicationServer server = new TestApplicationServer(new HttpContextController())

  @Test
  public void 'add a cookie and get a cookie'() {
    server.createRequest(post('context/add-cookie').followRedirects()).then({ res ->
      assertTrue(res.redirect)
      assertNotNull(res.cookies['foo'])
    })
  }

  @Test
  public void 'delete a cookie'() {
    server.createRequest(delete('context/remove-cookie')).then({
      assertTrue(it.ok)
      assertNull(it.cookies['foo'])
    })
  }

  @Test
  public void 'get body as'() {
    server.createRequest(post('context/get-body-as').json([username: 'me', password: 'pass'])).then({
      assertTrue(it.ok)
      assertNotNull(it.body)
      def form = Json.parse(it.body, HttpContextController.LoginForm)
      assertEquals('me', form.username)
      assertEquals('pass', form.password)
    })
  }

  @Test
  public void 'get path param'() {
    server.createRequest(get('context/get-path-param/10')).then({
      assertTrue(it.ok)
      int sum = ConverterRegistry.getConverter(Integer).convert(it.body)
      assertEquals(20, sum)
    })
  }
}
