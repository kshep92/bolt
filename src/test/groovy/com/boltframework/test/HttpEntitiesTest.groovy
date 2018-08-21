package com.boltframework.test

import com.boltframework.utils.Json
import org.junit.Test

import static org.junit.Assert.*
import static com.boltframework.utils.httpclient.HttpRequest.*
import static com.boltframework.utils.httpclient.HttpEntity.Cookie

class HttpEntitiesTest {

  @Test
  void 'construct request object'() {
    String url = "/users?name=kevin&sort=1"
    def request = get('/users').query('name', 'kevin').query('sort', 1);
    assertEquals(url, request.path)
    assertEquals('GET', request.method)
    request.contentType('application/json')
    assertEquals('application/json', request.headers.get('Content-Type').get(0))
    String json = Json.stringify([email: 'me@mail.com'])
    request = post('/signup').json(json)
    println(request)
    assertEquals(json, request.body)
    assertEquals('application/json', request.headers.get('Content-Type').get(0))
  }

  @Test
  public void 'create a cookie properly'() {
    String cookieString = 'admin=true; Max-Age=3000; Expires=Fri, 3 Aug 2018 17:22:32 GMT; Path=/'
    def cookie = Cookie.parse(cookieString)
    println(cookie)
    assertEquals('admin', cookie.get('name'))
    assertEquals('true', cookie.get('value'))
    cookie = Cookie.create(cookieString)
    assertEquals('admin', cookie.name())
    assertEquals('true', cookie.value())
    assertEquals(3000, cookie.maxAge())
    assertEquals('/', cookie.path())
  }
}
