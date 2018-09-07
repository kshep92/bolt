# Bolt Framework

Application development in an instant.

## Hello, world!

Below is an example of a basic Bolt application:

```java
public class MyApplication extends BoltApplication {
    
    @Override
    public void doRoutes(RouteRegistry rotues) {
        routes.get("/", ctx -> ctx.ok("Hello, world!"));
    }
    
    public static void main(String[] args) {
        Bolt.run(MyApplication.class) //Starts the app on port 3000
    }
}
```

And that's all it takes to start an application. By default, all applications run on `localhost:3000`. 

### Configuration

Maybe you don't want to use port 3000? You can change that by overriding the `configure()` method:

```java
public class MyApplication extends BoltApplication {
    
    @Override
    public void configure(ServerConfiguration config) {
        config.getHttp().port(9000);
    }
}
```

### Reading environment variables

Bolt comes with an `Env` class

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