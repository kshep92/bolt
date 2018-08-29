package com.boltframework.test

import app.*
import com.boltframework.config.ContextConfiguration
import com.boltframework.utils.httpclient.HttpRequest
import com.google.inject.Guice
import com.google.inject.Injector
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.mock

@ContextConfiguration(Configuration)
class InjectionTest extends BoltApplicationTest<MyApp> {
  UserService userService
  Injector injector = Guice.createInjector(new Configuration())


  @Before
  public void setUp() throws Exception {
    userService = mock(UserService)
    app.userController.userService = userService
  }

  @Test
  public void 'mock test'() {
    given(userService.save()).willReturn('Save from mock')
    assertEquals('Save from mock', userService.save())
  }

  @Test
  public void 'basic injection'() {
    def controller = injector.getInstance(UserController)
    assertNotNull('controller was not initialized', controller)
    given(userService.save()).willReturn('From mock')
    controller.setUserService(userService)
    println(controller.createUser())
  }

  @Test
  public void 'complex injection'() {
    def server = injector.getInstance(Server)
    assertNotNull('the client was not created', server)
    server.handleRequest('/users/create')
    given(userService.save()).willReturn('from mock')
    server.controller.setUserService(userService)
    server.handleRequest('/users/create')
  }

  @Test
  public void 'change value on server while running'() {
    given(userService.save()).willReturn("IT WORKED!")
    app.userController.userService = userService
    http.createRequest(HttpRequest.get('/users')).then({
      assertEquals(200, it.status)
      assertEquals('IT WORKED!', it.body)
      println(it)
    })
  }
}
