package com.boltframework.test

import com.boltframework.test.mocks.MockHttpServerResponse
import com.boltframework.web.HttpResponse
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.mockito.ArgumentMatchers.anyInt
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.ArgumentMatchers.contains
import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.containsString

class HttpResponseTest {

  HttpResponse response
  HttpServerResponse delegate
  Logger logger = LoggerFactory.getLogger(getClass())
  Map<String, String> headers
  String body
  Integer statusCode


  @Before
  public void setUp() throws Exception {
    headers = new HashMap<String, String>()
    body = ''
    statusCode = 0
    delegate = mock(MockHttpServerResponse)
    response = new HttpResponse(delegate, Json.mapper)
    when(delegate.setStatusCode(anyInt())).then({ statusCode = it.arguments[0] as Integer })
    when(delegate.putHeader(anyString(), anyString())).then({
      headers.put(it.arguments[0] as String, it.arguments[1] as String)
    })
    when(delegate.end(anyString())).thenAnswer({ body = it.arguments[0] as String })
  }

  @Test
  public void html() {
    response.html('<h1>Hello, world!</h1>')
    assertThat(body, containsString('Hello'))
    assertThat(headers.get('Content-Type'), containsString('text/html'))
  }

  @Test
  public void 'json with object'() {
    response.json([foo: 'bar'])
    assertThat(body, containsString('bar')) // I should use a JSON path matcher here
    assertThat(headers.get('Content-Type'), containsString('application/json'))
  }

  @Test
  public void 'json with string'() {
    response.json('{"foo":"bar"}')
    assertThat(body, containsString('bar'))
    assertThat(headers.get('Content-Type'), containsString('application/json'))
  }
}
