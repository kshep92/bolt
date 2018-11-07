package com.boltframework.test

import com.boltframework.data.ConverterRegistry
import com.boltframework.data.converters.LongConverter
import com.boltframework.test.mocks.MockHttpServerRequest
import com.boltframework.test.mocks.MockRoutingContext
import com.boltframework.web.HttpContext
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Cookie
import io.vertx.ext.web.RoutingContext
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.junit.Assert.*

class HttpContextTest {

  Logger logger = LoggerFactory.getLogger(getClass())
  RoutingContext delegate
  HttpContext context

  @Before
  void setup() {
    delegate = mock(MockRoutingContext)
    context = new HttpContext().withDelegate(delegate)
  }

  @Test
  void "get body as a particular type"() {
    MockHttpServerRequest mockRequest = mock(MockHttpServerRequest)
    given(mockRequest.getHeader(anyString())).willReturn("application/json")
    given(delegate.request()).willReturn(mockRequest)
    given(delegate.getBodyAsJson()).willAnswer({ new JsonObject([username: 'user', email: 'user@mail.com']) })
    assertNotNull(context.getBodyAs(UserForm))
    assertEquals('user', context.getBodyAs(UserForm).username)
    assertEquals('user@mail.com', context.getBodyAs(UserForm).email)
  }

  @Test
  void "get path parameter as a given type"() {
    ConverterRegistry.add(Long.class, new LongConverter())
    given(delegate.pathParam("id")).willReturn('1')
    def param = context.getPathParam('id', Long)
    assertNotNull(param)
    assertEquals(new Long(1), param)
  }

  @Test
  public void "removing a cookie"() {
    def cookie = Cookie.cookie("foo", "bar")
    when(delegate.getCookie(anyString())).thenReturn(cookie)
    context.removeCookie('foo')
    assertEquals('/', cookie.path)
    // No way to get the maxAge property
  }

  static class UserForm {
    String username
    String email
  }
}
