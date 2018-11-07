package com.boltframework.test

import com.boltframework.data.ConverterRegistry
import com.boltframework.data.converters.LongConverter
import com.boltframework.test.mocks.MockHttpServerRequest
import com.boltframework.web.HttpRequest
import io.vertx.core.MultiMap
import io.vertx.core.http.HttpServerRequest
import org.junit.Before
import org.junit.Test

import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.junit.Assert.*

class HttpRequestTest {

  HttpRequest request
  HttpServerRequest delegate

  @Before
  public void setUp() throws Exception {
    delegate = mock(MockHttpServerRequest)
    request = new HttpRequest(delegate)
  }

  @Test
  public void accept() {
    when(delegate.getHeader('Accept')).thenReturn('application/json')
    assertTrue('Full type', request.accepts('application/json'))
    assertTrue('Main type', request.accepts('*/json'))
    assertTrue('Subtype', request.accepts('application/*'))
    assertFalse('incorrect type', request.accepts('text/plain'))
  }

  @Test
  public void contentTypeMatches() {
    when(delegate.getHeader('Content-Type')).thenReturn('application/json')
    assertTrue('Full type', request.contentTypeMatches('application/json'))
    assertTrue('Main type', request.contentTypeMatches('*/json'))
    assertTrue('Subtype', request.contentTypeMatches('application/*'))
    assertFalse('incorrect type', request.contentTypeMatches('text/plain'))
  }

  @Test
  public void getParam() {
    ConverterRegistry.add(Long.class, new LongConverter())
    when(delegate.params()).thenReturn(MultiMap.caseInsensitiveMultiMap().add('id', '1'))
    def id = request.getParam('id', Long)
    assertNotNull(id)
    assertEquals(new Long(1), id)
  }

  @Test
  public void getQuery() {
    when(delegate.query()).thenReturn('foo=bar&baz=bim')
    assertEquals('bar from getQuery', 'bar', request.getQuery()['foo'])
    assertEquals('bim from getQuery', 'bim', request.getQuery()['baz'])
    assertEquals('bar from getQueryParam', 'bar', request.getQueryParam('foo'))
    assertEquals('bim from getQueryParam', 'bim', request.getQueryParam('baz'))
  }
}
