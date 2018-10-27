# Bolt Framework

:rotating_light: **BE ADVISED:** This project is in _super_ early development. It's only provided at this point as a concept/experiment.

Application development in an instant!

## About Bolt

Bolt was created to allow for rapid development of well-architected and easily testable Java web services. 

## Getting Started

Since Bolt is in super, pre-alpha early development, the best way to use Bolt in your project is via a Gradle include. Copy it to a folder on your PC then augment your project's `settings.gradle` file as follows:

```groovy
rootProject.name = 'myProjectName'

include 'bolt'

project(':bolt').projectDir = "path/to/bolt"
```

In your `build.gradle` add Bolt as a compile time dependency:

```groovy
dependencies {
    compile project(':bolt')
}
```

You're done!

## Your first application

A Bolt application is comprised of at least two parts - a Controller and a Web Service. A controller is nothing more than a regular Java class annotated with special annotations that tell Bolt how to wire up routing. Here's an example of a controller class:

```java
@RequestMapping("/") // 1
public class ApplicationController extends Controller { // 2
  
  @Route // 3
  public void index() {
    response().ok("Hello, world!");
  }
}
```

Not a few things:

1. The `@Route` annotation: This indicates the URL at which you can access this controller
2. Each controller must extend the Controller class
3. The `@Route` annotation: This tells Bolt to treat this method as a request handler

To use this controller, register it as part of a Web Service:

```java
class GreetingService extends WebService { // 1
    
    @Override
    public void addControllers(ControllerCollection controllers) { // 2
        controllers.register(ApplicationController.class);
    }
}
```

Note a couple things:

1. Your main class must extend `WebService`
2. You have to register your controllers with the Web Service by overriding the `addControllers()` method.

Finally, you can run your application by doing the following:

```java
class GreetingService extends WebService {
    
    public static void main(String[] args) {
        Bolt.createService(GreetingService.class).start();
    }
}
```

Your application will start and can be accessed at http://localhost:3000

## Routing

In the previous section we got a brief introduction to routing. Construction of your application's routing table is achieved by using the `@RequestMapping` annotation on your controllers, the `@Route` annotation on any of the controller methods and finally registering the controller in the main Web Service.

### The @RequestMapping annotation

This annotation is used to tell the route builder which prefix to apply to the routes in this controller. Let's look at an example:

```java
@RequestMapping("/greeting")
class GreetingController extends Controller {
    
    @Route("/say-hello")
    public void sayHello() {
        resonse().ok("Hello!")
    }
}
```

To access the route above, you have to visit `/greeting/say-hello`. 

### The @Route annotation

The `@Route` annotation allows you to create clean and highly specific routes.

| Property | Type         | Default          | Function                                                     |
| -------- | ------------ | ---------------- | ------------------------------------------------------------ |
| value    | `String[]`   | `{"/"}`          | Specify URL patterns that this route should match. E.g. `/home`, `/about` |
| pattern  | `String[]`   | `{}`             | Specify URL patterns that this route should match in the form of regular expressions. E.g. `/(?!login|logout).*` |
| consumes | `String[]`   | `{}`             | A list of MIME types that the route should accept. E.g. `application/json` |
| produces | `String[]`   | `{}`             | A list of MIME types that the route produces. E.g. `text/plain` |
| method   | `HttpMethod` | `HttpMethod.GET` | The HTTP verb associated with this route. See [Routing by HTTP Method](#routing-by-http-method) for more details. |
| order    | Integer      | 0                | The order this route is added to the routing table           |



#### Routing by exact path

A route can be configured to match an exact URI pattern.  In the above example, a `GET` request to `/greeting/say-hello` will yield a plain text greeting.

#### Routing by path beginning with something

If you wish to be a little less specific with your URI pattern, you can use wildcard characters. For example, the below route will match `/foo/bar`, `/foo/baz`, etc.

```java
@Route("/foo/*")
public void fooResponse() {
    response().ok("Foo");
}
```

#### Routing by HTTP method

By default, routes are configured to match HTTP GET requests. To match a different HTTP method, specify the `method`property on your route. In the below example, we will configure a route to respond to any POST requests:

```java
@Route(value = "/post", method = HttpMethod.POST)
public void create() {
    String body = context().getBodyAsString();
    response().ok("Received body: " + body);
}
```

All HTTP methods are supported:

* DELETE
* GET (default)
* HEAD
* OPTIONS
* PATCH
* POST
* PUT

You can also create a route using convenience annotations for each HTTP method:

* @Delete
* @Get
* @Head
* @Options
* @Patch
* @Post
* @Put

All of these annotations take the same parameters as the regular `@Route` annotation except `method`.

#### Path parameters

You can specify parameters in your routes as well:

```java
@Route("/users/:id")
public void create() {
    Long id = Long.parseLong(context().getPathParam("id"));
    User user = userDao.findById(id)
    response().ok("Received body: " + user.toString());
}
```

#### Routing with regular expressions

Routes can also be specified using regular expressions. In the following example, we want to match every request except requests that contain the word "admin" in their URI:

```java
@Route(pattern = "/(?!admin).*")
public void publicRoute() {
    response().ok("You're in the regular user space.");
}
```

#### Routing based on MIME type of the request

If you'd like your application to choose a route depending on the `Content-Type` header of the request, set the `consumes` property on the route:

```java
@Route(value = "/json", consumes = "application/json")
public void jsonRoute() {
    response().ok().json("I only accept JSON requests.");
}
```

You can specify more than one content type.

#### Routing based on MIME types desired by the client

Similar to the above, you tell your application to use a route depending on the type of content the client expects. Do this by setting the `produces` property on the route:

```java
@Route(value = "/plain", produces = "text/plain")
public void textRoute() {
    response().ok("I produce plain text only");
}
```

You can also specify multiple content types here. 

#### Specifying route order

Generally, you'll want make your URI patterns for your routes as specific as possible since there is no guarantee of the order the route definitions will be read at application startup time. For example, if you have the following routes:

```java
@Get("/home") // Potentially unreachable 😟
public void home() {
    // ...
}

@Get("/*")
public void catchAll() {
    // ...
}
```

Although the `catchAll()` route is defined after the `home()` route, the JVM *may* read the routes in reverse order which would mean that the `home()` route may be unreachable. To solve this issue, specify an order on the more general `catchAll()` route that is "lower" than the `home()` route.

By default, all routes are given an order of 0. Routes with higher order are pushed lower down the routing stack.

 ```java
@Route(value = "/*", order = 1) // WIll be read last in the controller
public void catchAll() {
    // ...
}
 ```

**Tip:** Try to think of route order as array indexes where 0 is the first element in the array and *n* is the last index. The higher the number *n* is, the further down in the array it is.

**Note:** Route ordering is done on a per controller basis. A very general/catch-all route may be ordered last in one controller, but can make the rest of your routes unreachable depending on the order in which you register your controllers with your Web Service. Plan your routes carefully for best results.

### Configuration

Maybe you don't want to use port 3000? You can change that by supplying a port number to the `start()` method:

```java
public class Main extends WebService {
  
  // Initialization code
  
  public static void main(String[] args) {
    Bolt.createService(Main.class).start(3000); //Starts the app on port 3000
  }
}
```

### Reading environment variables

Bolt comes with an `Env` class which allows you to read environment variables. Use the `getString()`, `getBoolean()` and `getInt()` methods to retrieve String, Boolean and Integer properties accordingly.

#### Am I in Dev or Prod mode?

The `app.mode` environment/system property can be used to determine the mode the application is running in. Use `Env.getString("app.mode")` to get this value, or use the shortcuts `Env.isDev()` and `Env.isProd()` which will return Boolean values. E.g.:

```java
if(Env.isProd()) useProductionDatabase();
else useDevelopmentDatabase();
```



## Testing

Bolt makes creating integration tests for your application super easy. Simply extend the 
`com.boltframework.test.BoltApplicationTest` class.

```java
public class MyApplicationTest extends BoltApplicationTest<MyApplication> {
  /* Unit tests here */
}
```

From this class, you will have access to a handy `com.boltframework.utls.httpclinet.HttpClient` instance as well as 
an instance of the type passed in (in our case an instance of the `MyApplication` class). 

Below is an example of your you can create a basic smoke test

```java
/* MyApplication.java */
public class MyApplication extends BoltApplication {
    private string greeting = "OK";

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
    
    @Override
    public void doRoutes(RouteRegistry routes) {
        routes.get("/", ctx -> {
            ctx.ok("OK");
        });
    }
  
}
```

```java
/* MyApplicationTest */
import static com.boltframework.utils.httpclient.HttpRequest.*;
import static org.junit.Assert.*;

public class MyApplicationTest {
  
    @Test
    public void smokeTest() {
        @ClassRule
        public static TestServer<MyApplication> testServer = new TestServer<>();
        // If the server isn't running it will start it and keep it running
        // for the duration of the class
        testServer.createRequest(get("/")).then(response -> { 
            assertEquals(200, response.getStatus());
            assertEquals("OK", response.getBody());
        });
    }
    
    @Test
    public void replaceGreetingTest() {
        // getApplication returns an instance of MyApplication
        testServer.getApplication().setGreeting("Hello");
        http.createRequest(get("/")).then(response -> { 
            assertEquals(200, response.getStatus());
            assertEquals("Hello", response.getBody());
        });
    }
  
}

```

Having access to an instance of your running application gives you the ability to change properties and even inject mock implementations of dependencies into you application during testing.

**Tip:** Explore the `com.boltframework.utils.httpclient` package to learn about creating requests and examining the response.