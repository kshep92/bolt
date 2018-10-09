package com.boltframework.test

import app.*
import HttpRequest
import com.google.inject.*
import Names
import Before
import Test
import LoggerFactory

import static Assert.assertEquals
import static Assert.assertNotNull
import static BDDMockito.given
import static Mockito.mock

class InjectionTest {
  UserService userService
  Injector injector = Guice.createInjector(new Configuration())

  @Before
  public void setUp() throws Exception {
    userService = org.mockito.Mockito.mock(UserService)
    app.userController.userService = userService
  }

  @Test
  public void 'mock test'() {
    org.mockito.BDDMockito.given(userService.save()).willReturn('Save from mock')
    org.junit.Assert.assertEquals('Save from mock', userService.save())
  }

  @Test
  public void 'basic injection'() {
    def controller = injector.getInstance(UserController)
    org.junit.Assert.assertNotNull('controller was not initialized', controller)
    org.mockito.BDDMockito.given(userService.save()).willReturn('From mock')
    controller.setUserService(userService)
    println(controller.createUser())
  }

  @Test
  public void 'complex injection'() {
    def server = injector.getInstance(Server)
    org.junit.Assert.assertNotNull('the client was not created', server)
    server.handleRequest('/users/create')
    org.mockito.BDDMockito.given(userService.save()).willReturn('from mock')
    server.controller.setUserService(userService)
    server.handleRequest('/users/create')
  }

  @Test
  public void 'change value on server while running'() {
    org.mockito.BDDMockito.given(userService.save()).willReturn("IT WORKED!")
    app.userController.userService = userService
    http.createRequest(HttpRequest.get('/users')).then({
      org.junit.Assert.assertEquals(200, it.status)
      org.junit.Assert.assertEquals('IT WORKED!', it.body)
      println(it)
    })
  }

  @Test
  public void packageTest() {
    String className = Configuration.name
    String packageName = className.substring(0, className.lastIndexOf('.'))
    LoggerFactory.getLogger('tst').debug('{}.*', packageName)
  }

  @Test
  public void singletonTest() {
    def injector = Guice.createInjector(new ConfigModule())
    def peter = injector.getInstance(Person)
    org.junit.Assert.assertNotNull(peter)
    peter.firstName = 'Peter'
    peter.lastName = 'Marshall'
    peter = injector.getInstance(Person)
    org.junit.Assert.assertEquals(1, peter.instanceCounter)
    org.junit.Assert.assertEquals('Peter', peter.firstName)
    org.junit.Assert.assertEquals('Marshall', peter.lastName)
    injector.createChildInjector(new ConfigModule2())
    peter = injector.getInstance(Person)
    org.junit.Assert.assertEquals(1, peter.instanceCounter)
    org.junit.Assert.assertEquals('Peter', peter.firstName)
  }

  @Singleton
  static class Person {
    String firstName
    String lastName
    Address address
    static int instanceCounter = 0

    @Inject
    Person(Address address) {
      instanceCounter += 1
      this.address = address
    }
  }

  static class Address {
    String street
  }

  class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    public Address address() {
      return new Address(street: 'Chancery Lane')
    }
  }

  class ConfigModule2 extends AbstractModule {

    @Override
    protected void configure() {
      bind(String).annotatedWith(Names.named("greeting")).toInstance("Hello, world")
    }
  }
}
