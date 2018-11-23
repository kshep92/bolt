# ⚡ Bolt Framework ⚡

Application development in an instant!

>  :rotating_light: **BE ADVISED** :rotating_light: This project is in _super_ early development. It's only provided at this point as a concept/experiment.

### TODO

- [ ] Create ExceptionHandler logic
- [ ] WebSocket endpoints
- [x] Configurable JSON serialization/deserialization
- [x] Configurable body parser (Right now body sizes are limited to 10MB :see_no_evil: )
- [ ] Allow addition of type converters
- [ ] Allow classpath loading of templates
- [x] CORS (Use the Middleware class)
- [ ] Write more tests!!!!
- [ ] Complete documentation

## Desired Enhancements

- [ ] Use Classpath scanning/annotation processing for controller discovery at startup
- [ ] Use Lihtbend Config (HOCON) for managing configuration
- [ ] Choose between Freemarker and Pebble for a template engine (or continue with the `TemplateEngine` interface and let users define their own)

## About Bolt

Bolt was created to allow for rapid development of well-architected and easily testable Java web applications. Bolt is simply a wrapper around the [Vert.x Web](https://vertx.io/web) HTTP library, the sole purpose of which is to provide developers with another paradigm of developing web apps with Vert.x.

## Getting Started

Since Bolt is in super, pre-alpha early development, the best way to use Bolt in your project is via cloning the repository then doing a Gradle include or installing it into your local Maven repository. 

### Installing to local Maven repository

```
git clone https://github.com/kshep92/bolt.git
cd bolt
./gradlew install
```

```groovy
/* build.gradle */

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compile "com.reviselabs:boltframework:0.1.0"
}
```

### Gradle include

`git clone https://github.com/kshep92/bolt.git`

```groovy
/* settings.gradle */

rootProject.name = 'myProjectName'

include 'bolt'

project(':bolt').projectDir = file("path/to/bolt")
```

```groovy
/* build.gradle */

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

Note a few things:

1. The `@RequestMapping` annotation: This indicates the URL at which you can access this controller
2. Each controller must extend the `com.boltframework.web.mvc.Controller` class
3. The `@Route` annotation which tells Bolt to treat this method as a request handler

To use this controller, register it as part of a [Web Service](#web-services):

```java
class GreetingService extends WebService { // 1
    
    @Override
    public void addControllers(ControllerCollection controllers) { // 2
        controllers.register(ApplicationController.class);
    }
}
```

Note a couple things:

1. Your main class must extend `com.boltframework.web.WebService`
2. You have to register your controllers with the Web Service by overriding the `addControllers()` method.

:memo: **Note:** Point 2 May go away in a future version.

Finally, you can run your application by doing the following:

```java
class GreetingService extends WebService {
    
    public static void main(String[] args) {
        Bolt.createService(GreetingService.class).start();
    }
}
```

Your application will start and can be accessed at http://localhost:3000

#### A complete application

```java
public class MyApplication extends WebService {
    
    @RequestMapping("/")
    public static class ApplicationCtrl extends Controller {
        
        @Route("/")
        void index() {
            response().ok().html("<h1>Hello, world!</h1>");
        }
    }
    
   public void addControllers(ControllerRegistry controllers) {
       controllers.addController(ApplicationCtrl.class);
   }
    
    public static void main(String[] args) {
        Bolt.createService(MyWebService.class).start(1234) // Start on port 1234
    }
}
```

## The components of a Bolt application

A Bolt application is really just a collection of special classes which are all orchestrated together by the use of a Web Service definition. Below is a breakdown of what these components are.

### Controllers

A controller is simply a class whose methods are accessible via HTTP end points. Your application can have as many controllers as required. 

Below is an example of a `Students` controller in a school's student registration web app:

```java
@RequestMapping("/students")
public class StudentsCtrl extends Controller {
    
    private StudentDAO dao = new StudentDAO();
    
    @Route("/")
    public void index() {
        response().json(dao.allStudents());
    }
    
    @Route(value = "/", method = HttpMethod.POST)
    public void create() {
        Student student = context().getBodyAs(Student.class);
        Long id = dao.save(student);
        response().ok("Student created.");
    }
    
    @Route(":id")
    public void show() {
        Long id = context().getPathParam("id", Long.class);
        Student student = dao.find(id);
        response().json(student);
    }
}
```

In the above example, we see that we have a controller annotated with `@RequestMapping` which will allow us to access this controller via the `/students` route. 

Each of the methods in the controller use the `@Route` annotation to specify their routing details. For example, the `index()` method can be accessed using `/students/` and the `show()` method can be accessed via `/students/1`  where the "1" is an ID of a student.

### Interceptors

An interceptor is a class that implements the `Consumer<HttpContext>` interface and is run before any of the routes you define on your controllers.

Below is an example of an authorization interceptor:

```java
public class AuthInterceptor implements Consumer<HttpContext> {
    
    private UserDAO dao = new UserDAO();
    
    @Override
    public void accept(HttpContext ctx) {
        Cookie tokenCookie = ctx.getCookie("authToken");
        if(tokenCookie == null) ctx.response().unauthorized("You must log in.");
        else {
            ctx.put("user", dao.find(tokenCookie.getValue()));
            ctx.next();
        }
    }
    
}
```

In the above example we check to see if the request has a cookie by the name of  "authToken". If it doesn't we send back a `401 UNAUTHORIZED` response with a message telling the user to log in.

If the cookie is present, we use its value to find the associated user, add it to the context and then call the next interceptor or route handler using the `next()` method.

### Exception handlers

**[TODO]**

### Static resource handlers

Static resource handlers allow you to serve static resources. This is more of a configuration operation, and is done by overriding the relevant method in your Web Service.

### Web Services

Bolt uses the concept of a web service to define how all the components of your application - controllers, interceptors, exception handlers and static resource handlers - all come together. A Bolt application consists of only one web service which is a class that extends  `com.boltframework.web.WebService`.

Your web service definition must implement certain methods which tell Bolt how to orchestrate the components of your application.

#### Registering Controllers

To register a controller, override the `addControllers` method of your Web Service. This method receives an instance of `com.boltframework.web.routing.ControllerCollection`. See below how it is used to register the Student controller we created earlier:

```java
@Override
public void addControllers(ControllerCollection controllers) {
    controllers.addController(StudentsCtrl.class);
}
```

:memo: Note how we pass the class of the controller and not an instance.

#### Registering Interceptors

To register an interceptor with your Web Service, override the `addInterceptors` method. This method receives an instance of `com.boltframework.web.routing.InterceptorCollection`. Below is an example of how to use it:

```java
@Override
public void addInterceptors(InterceptorCollection interceptors) {
    interceptors.addInterceptor(new AuthInterceptor());
    // Also valid
    interceptors.addInterceptor(ctx -> {
        System.out.println("I'm an interceptor too!");
        ctx.next();
    });
}
```

The above will cause all our application's routes to be intercepted by those two interceptors. 

Each call to `addInterceptor` on the `InterceptorCollection` will return a new instance of `com.boltframework.web.routing.InterceptorProperties`. This class has two methods - `addPath` and `addPathRegex` which you can use to specify which routes should be intercepted by an interceptor. See below:

```java
@Override
public void addInterceptors(InterceptorCollection interceptors) {
    interceptors.addInterceptor(ctx -> {
        System.out.println("The ID of the student is: " + ctx.getPathParam("id"));
        ctx.next();
    }).addPath("/students/:id");
    interceptors.addInterceptor(new AuthInterceptor()).addPathRegex("/(?!auth).*");
}
```

The first interceptor will be called before the `/students/:id` route. The second will be called before any route not matching `/auth`.

#### Configuring static resources

Your application can be configured to serve static resources by overriding the `addResourceHandlers` method. This method receives an instance of `com.boltframework.web.routing.ResourceHandlerCollection`.  

The `addHandler` method on the `ResourceHandlerCollection` instance takes two parameters:

1. The URL pattern for the resource handler
2. The directory on disk or on the classpath to serve.

It returns an instance of `com.boltframework.web.routing.ResourceHandlerProperites` which you can configure to set options for caching.

```java
@Override
public void addResourceHandlers(ResourceHandlerCollection handlers) {
    // Serve the www/public directory for any requests coming in for /public/*
    handlers.addHandler("/public/*", "www/public");
}
```

Under the covers it uses a stripped down version of an `io.vertx.ext.web.handler.StaticHandler` so [read their docs](https://vertx.io/docs/vertx-web/java/#_serving_static_resources) to get some further insight on its inner workings.

#### A full web service

```java
public class MyWebService extends WebService {
    
    @Override
    public void addInterceptors(InterceptorCollection interceptors) {
        interceptors.addInterceptor(new AuthInterceptor()).addPathRegex("/(?!auth).*");
    }
    
    @Override
    public void addControllers(ControllerCollection controllers) {
        controllers.addController(StudentsCtrl.class);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerCollection handlers) {
        handlers.addHandler("/public/*", "www/public");
    }
}
```

## Routing

In the previous section we got a brief introduction to routing. Construction of your application's routing table is achieved by using the `@RequestMapping` annotation on your controllers, adding the `@Route` annotation to any of the controller methods and finally registering the controller in the main Web Service.

> ​ :bulb: If you find the content of this section seems familiar, it's because it's basically what you can find in the routing section of the official [Vert.x Web docs](https://vertx.io/docs/vertx-web/java/#_routing_by_exact_path).



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

:bulb: **Tip: **The preceding slash is optional when defining your routes. You could have just as well said `@RequestMapping("greeting")` and `@Route("say-hello")`. It's just a convenience feature.

### The @Route annotation

The `@Route` annotation allows you to create clean and highly specific routes.

| Property | Type         | Default          | Function                                                     |
| -------- | ------------ | ---------------- | ------------------------------------------------------------ |
| value    | `String[]`   | `{"/"}`          | Specify URL patterns that this route should match. E.g. `/home`, `/about` |
| pattern  | `String[]`   | `{}`             | Specify URL patterns that this route should match in the form of regular expressions. E.g. `/(?!login|logout).*` |
| consumes | `String[]`   | `{}`             | A list of MIME types that the route should accept. E.g. `application/json` |
| produces | `String[]`   | `{}`             | A list of MIME types that the route produces. E.g. `text/plain` |
| method   | `HttpMethod` | `HttpMethod.GET` | The HTTP verb associated with this route. See [Routing by HTTP Method](#routing-by-http-method) for more details. |
| order    | Integer      | 0                | The order this route is added to the routing table at application startup. Bolt will use all `@RequestMapping` and `@Route` annotations it finds to construct the routing table. |

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

#### Capturing path parameters

You can specify parameters in your routes as well using the colon (:) pattern:

```java
@Route("/users/:id")
public void create() {
    Long id = context().getPathParam("id", Long.class);
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

See [Capturing path parameters with regular expressions](https://vertx.io/docs/vertx-web/java/#_capturing_path_parameters_with_regular_expressions) and [Using named capture groups](https://vertx.io/docs/vertx-web/java/#_using_named_capture_groups) on the Vert.x docs for some more tricks you can do with regular expressions in your routes.

#### Routing by HTTP method

By default, routes are configured to match HTTP GET requests. To match a different HTTP method, specify the `method` property on your route. In the below example, we will configure a route to respond to any POST requests at the `/post` path:

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

#### Specifying route order

Generally, you'll want make your URI patterns for your routes as specific as possible since there is no guarantee of the order the route definitions will be read at application startup time. For example, if you have the following routes:

```java
@Get("/home")
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

Try to think of route order as array indexes where 0 is the first element in the array and *n* is the last index. The higher the number *n* is, the further down in the array it is.

:memo: **Note:** Route ordering is done on a per controller basis. A very general/catch-all route may be ordered last in one controller, but can make the rest of your routes unreachable depending on the order in which you register your controllers with your Web Service. Plan your routes carefully for best results.

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

Feel free to combine route criteria to make your route as specific as possible:

```java
@Get(value = "/profile", consumes = "application/json", produces = "text/csv")
public void profile() {
    // Accept JSON, return some CSV content
}
```

### The HTTP Context

In your controller classes, interceptors and exception handlers, you have access to the HTTP Context of each request. In controllers, you can access this via the `context()` method, and for [interceptors](#interceptors) and exception handlers it is passed as the only argument to the `accept()` method. Below is a brief on some of the methods you can use:

```java
/* Create a flash cookie named "flash" with a message. */
HttpContext flash(String message);
    
/* Add a cookie */
HttpContext addCookie(io.vertx.ext.web.Cookie cookie);
    
/* Add a cookie with some default values set for path and maxAge */
HttpContext addCookie(String name, String value);

/* Fail a request. Normally used in interceptors */
void fail(Integer statusCode);

/* Get an object from the context. Mostly used in interceptors */
Object get(String key);

/* Convert the request body to a given type. Only works for JSON requests for now */
T getBodyAs(Class<T> bodyType);

/* Get a JSON request body as a JsonObject */
JsonObject getBodyAsJson();

/* Get the body as a simple string */
String getBodyAsString();

/* Get a cookie from the request */
Cookie getCookie(String name);

/* Get all cookies from the request */
Set<Cookie> getCookies();

/* Get all file uploads sent from the client */
Set<FileUpload> getFileUploads();

/* Get a header from the request */
String getHeader(String name);

/* Get a path parameter from a path, e.g. /users/:name */
String getPathParam(String name);
    
/* Get a path parameter as a particular type, e.g. Long, Boolean, Doublet, etc. */
T getPathParam(String name, Class<T> type);

/* Get all path parameters */
Map<String, String> getPathParams();

/* Get the HttpRequest */
HttpRequest getRequest();

/* Get the HttpResponse */
HttpResponse getResponse();

/* Proceed to the next matching route/interceptor. Mostly used in interceptors */
void next();

/* Add something to the HttpContext */
HttpContext put(String key, Object value);

/* Remove something from the HttpContext */
HttpContext remove(String key);

/* Remove a cookie from the context and from future requests */
HttpContext removeCookie(String name)

```

There's a bit more information about the HTTP Context (Routing Context) [here](https://vertx.io/docs/vertx-web/java/#_basic_vert_x_web_concepts).

### The HttpRequest object

You have access to the incoming HTTP request via the `request()` method in your controllers and via the `HttpContext#getRequest()` method in your interceptors . Below are some of the useful methods on the `HttpRequest` object:

```java
/* Check the Accept header for a MIME type e.g. text/html or text/* */
public Boolean accepts(String contentType);

/* Ensure that the Content-Type header matches a particular
MIME type. e.g. application/json */
Boolean contentTypeMatches(String contentType);

/* Get the absolute URL of the request */
String getAbsoluteUrl();

/* Get a request header */
String getHeader(String name);

/* Get all request headers */
MultiMap getHeaders();

/* Get the host portion of the URL */
String getHost();

/* Get the IP address of the client */
String getIpAddress();

/* Get the HTTP method of the request */
HttpMethod getMethod();

/* Get all parameters submitted with the request - query, path and form */
MultiMap getParams();

/* Get the path portion of the request URL e.g. /home */
String getPath();

/* Get a parameter from the request */
String getParam(String name);

/* Get a parameter from the request as a particular type - Long, Double, Float, etc. */
T getParam(String name, Class<T> type);

/* Get the port portion of the request URL */
Integer getPort();

/* Get the query parameters as a Map */
Map<String, String> getQuery();

/* Get a query parameter */
String getQueryParam(String param);

/* Get a query parameter cast to particular type. */
T getQueryParam(String param, Class<T> type);

/* A convenience method to Get the User-Agent header */
String getUserAgent();

/* Determine if the request is coming from a secure context - https */
Boolean isSecure();
```

### The HttpResponse object

After receiving a request, you usually send a response back to the client through the `HttpResponse` object which can be accessed via the `response()` method in controllers and via the `HttpContext#getResponse()` method in interceptors.

Below are some of the useful methods on the `HttpResponse` object:

```java
/* Add a header to the HTTP response */
HttpResponse addHeader(String name, String value);

/* Set the response status to 400 */
HttpResponse badRequest();

/* Set the response status to 400 and send a plain text message  */
void badRequest(String body);

/* Set the response status to 500 */
HttpResponse error();

/* Set the response status to 500 and send a plain text message */
void error(String body);

/* End your handling of the response and send it */
void end();

/* Set the response status to 401 */
HttpResponse forbidden();

/* Set the response status to 401 and send a plain text message */
void forbidden(String body);

/* Send an HTML response */
void html(String htmlString);

/* Set the content-type of the response to text/html */
HttpResponse html();

/* Serialize an object to a JSON string and send a JSON response */
void json(Object entity);

/* Send a JSON response */
void json(String json);

/* Set the content-type of the response to applicaton/json*/
HttpResponse json();

/* Set the response status to 404 */
HttpResponse notFound();

/* Set the response status to 404 and send a plain text message  */
void notFound(String body);

/* Set the response status to 200 */
HttpResponse ok();

/* Set the response status to 200 and send a plain text message  */
void ok(String body);

/* Send a 301 redirect */
void redirect(String path);

/* Sends a redirect, but allows you to choose the code */
void redirect(String path, Integer code);

/* Render a template */
void render(String templateName)

/* Render a template and send an HTML response */
void renderHtml(String templateName);

/* Send a response */
void send(String body);

/* Send a file download */
void sendFile(String fileName);

/* Give the response a chunked encoding */
HttpResponse setChunked(Boolean chunked);

/* Set the Content-Type header */
HttpResponse setContentType(String contentType);

/* Set the status code */
HttpResponse setStatusCode(Integer status);

/* Set the status code */
HttpResponse setStatusCode(HttpResponseStatus status)

/* Set the response status to 501 */
void todo();

/* Set the response status to 501 and send a plain text message */
void todo(String message);

/* Set the response status to 401 */
HttpResponse unauthorized();

/* Set the response status to 401 and send a plain text message */
void unauthorized(String message);

/* Write a chunk to the HTTP response */
HttpResponse write(String chunk);

/* Write a chunk and a new line character to the HTTP response */
HttpResponse writeLine(String chunk);
```



## Handling forms

Accepting input from your users is a standard feature in any app. Bolt makes the acceptance, validation and the passing on of that data to your domain objects really easy by the use of the `com.boltframework.data.forms.Form` class.

The Form class makes use of the [Hibernate Validator](http://hibernate.org/validator/) for standards compliant JSR-303 field validation and it also allows you to add your own validation logic. Let's look at an example of a typical user registration form:

```java
public class SignupForm extends Form {
    @NotBlank 
    @Email
    private String email;
    @NotBlank
    private String fullName;
    @NotBlank
    @Size(min = 8)
    private String password;
    private String passwordConfirm;
    
    
    // Getters and setters.
}
```

In the form you'll notice that we extend the `Form` class and use typical JSR-303 bean validation annotations for our fields. Next, we create a controller method for handling user submitted data:

```java
@RequestMapping("signup")
public class RegistrationCtrl extends Controller {
    
    private UserDAO dao = new UserDAO();
    
    @Post
    public void signup() {
        SignupForm form = context().getBodyAs(SignupForm.class);
        if(!form.valid()) {
            response().badRequest().json(form.getErrors());
            return;
        }
        //TODO: Save the user details
        
    }
}
```

In the `signup()` method, you can see that we first parsed the response body as an instance of `SignupForm` then we called the `valid()` method which would check the validation constraints. This method returns false if any of the constraints are violated. 

If there are any constraint violations, you can inspect them using the `Form#getErrors` method which would return a Map of the erroneous fields and their error messages.

If the data submitted was valid, you can carry on processing the form.

### Custom validation

If you look at the structure of our form you'll notice that there's a field we're not running any validations on the `passwordConfirm` field. What we would like to ensure is that the value in this field matches the value entered in the `password` field.

Since there isn't a JSR-303 annotation for this type of logic, we'll have to implement our own (validation logic, not annotation, although you could totally do that if you want.)

All we have to do is override the `Form#validate` method and return a `String` if the validation fails or `null` if it passes. Let's see how it's done:

```java
public class SignupForm extends Form {
    // Previously defined fields
    // Getters and setters
    
    @Override
    public String valdate() {
        return !passwordConfirm.equals(password) ? "Passwords do not match." : null;
    }
    
}
```

Now if the passwords don't match, the form's error message would be "Passwords do not match" and you will be able to retrieve this via the `Form#getErrorMessage` method.

### Transferring the data

Some developers prefer to separate data validation logic from their domain models by utilizing something called a Data Transfer Object or DTO. This is a good approach until it comes time to map the fields from their DTO to an actual domain model. The `Form` class comes with a few useful methods for helping with that.

```java
/* Copy the form properties to another bean. It won't copy any form properties whose value is null */
Boolean copyPropertiesTo(Object target);

/* Same as previous method but you can choose to include null values from the form */
Boolean copyPropertiesTo(Object target, Boolean ignoreNulls);

/* Copy properties from a bean to the form instance. It won't copy any bean property whose value is null */
Boolean copyPropertiesFrom(Object source);

/* Same as above, but you can choose to include null values from the source bean */
Boolean copyPropertiesFrom(Object source, Boolean ignoreNulls);

/* Return a new instance of a given bean which will have its properties set based on the properties of the fom */
T getInstanceOf(Class<T> tClass)

```

A few things to note:

1. Any property with null value is excluded from the data mapping by default.
2. If the destination bean does not have certain properties defined on the source bean, those properties will be ignored.
3. The source and destination beans need to follow the standard for a Java properties - private instance field with appropriately named getter and setter with the setter returning void.

The `Form` class utilizes standard Java Bean Introspection to achieve this functionality.

Given the above, let's look at an example of how you would use one of these map methods:

```java
/* User domain model */
public class User {
    private Long id;
    private String email;
    private String fullName;
    private String passwordHash;
    
    // Getters and setters
}
```

```java
/* Our controller */
@RequestMapping("/signup")
public class SignupCtrl extends Controller {
    
    private UserDAO dao = new UserDAO();
    
    @Post
    public void signup() {
        SignupForm form = context().getBodyAs(SignupForm.class);
        
        // Do validation
        
        /* Returns a User instance with email and fullName populated. */
        User user = form.getInstanceOf(User.class);
        dao.save(user);
        response.ok().json(user);
    }
}
```

## Rendering templates

Bolt comes with a barebones implementation of the Freemarker template engine. By default, this template engine looks for templates with a `.ftl` extension in the `www/views` directory.

To render a template, simply call the `render()` or `renderHtml()` method on an `HttpResponse`:

```java
@Get
public void index() {
    response().renderHtml('home.ftl') // Will look for www/views/home.ftl
}
```

:memo: Inclusion of the template extension is optional. 

### Passing data to templates

The `HttpContext` class has a mechanism for storing arbitrary data that is accessed via it's `put()` method. Any data stored in the context can be accessed from the template.

```html
<!-- The template, greet.ftl -->
<p>
    Hello, ${name}!
</p>
```

```java
@Get('greet')
public void greet() {
    context().put("name", "John").getResponse().renderHtml("greet");
}
```

## Dependency Injection

In our examples thus far we've been instantiating certain class properties using the `new` keyword. This approach is fine for simple example code, but it makes for less testable code overall - not great for production applications.

Bolt encourages the use of [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) to promote code testability, and makes use of the [Guice](https://github.com/google/guice) dependency injection library under the hood for managing application-wide dependencies. 

To begin, create a class that extends `com.boltframework.context.CoreDependencies`:

```java
public class MyModule extends CoreDependencies {
    
    @Singleton
    public EntityManagerFactory entityManagerFactory() {
        // Return an instance of EntityManagerFactory
    }
}
```

The above module manages an `EntityManagerFactory` dependency.  To make Bolt aware of this dependency, use the `Bolt#withContext` method when creating your Bolt application:

```java
public static void main(String[] args) {
    Bolt.createService(MyService.class).withContext(new MyModule()).start();
}
```

### Customizing `CoreDependencies`

With access to the `CoreDependencies` class you can override many of the defaults that come with Bolt such as the template engine and the JSON object mapper. Just override the relevant methods. Below, we see how to change the default file extension for templates on template engine:

```java
public class MyDeps extends CoreDependencies {
    
    @Override
    public TemplateEngine templateEngine() {
        return super.templateEngine().defaultExtension(".template");
    }
}
```

While we're on the topic of template engines, you can even use your own custom engine if you desire:

```java
@Override
public TemplateEngine templateEngine() {
    return new MyCustomTemplateEngine();
}
```

You can further customize the core defaults by overriding the following methods:

```java
/* Returns the single instance of Vertx that powers the entire application. */
Vertx vertx();

/* Returns an instance of the Freemarker template engine by default */
TemplateEngine templateEngine();

/* Returns an instance of a Jackson ObjectMapper */
ObjectMapper objectMapper();

/* Return a custom body handler with a different upload directory and other stuff */
BodyHandler bodyHandler();
```

### Utilizing dependency modules

Now your can refactor your controllers and other domain objects to make use of the managed dependencies.

Given that we chose to use `EntityManagerFactory` as part of our example, let's see how it can be used in a DAO context:

```java
public class UserDAO {
    private EntityManagerFactory emf;
    
    @Inject
    public UserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
```

Now our `UserDAO` relies on an instance of `EntityManagerFactory` to be injected.

### Dependencies and Controllers

To use our `UserDAO` in a controller, we can simply pass it as an injected dependency to our controller's constructor:

```java
@RequestMapping("/users")
public class UserCtrl extends Controller {
    private UserDAO dao;
    
    @Inject
    public UserCtrl(UserDAO dao) {
        this.dao = dao;
    }
}
```

### Dependencies and Web Services

You can inject dependencies into your Web Service definition in a similar manner. For example, let's inject our `UserDAO` into our web service so we can use it in an interceptor:

```java
public class MyService extends WebService {
    private UserDAO dao;
    
    @Inject
    public MyService(UserDAO dao) {
        this.dao = dao;
    }
    
    public void addInterceptors(InterceptorCollection interceptors) {
        interceptors.addInterceptor(ctx -> {
            Cookie tokenCookie = ctx.getCookie("authToken");
            if(tokenCookie == null) ctx.response().unauthorized("You must log in.");
            else {
              ctx.put("user", dao.find(tokenCookie.getValue()));
              ctx.next();
            }
         });
    }
}
```

If this approach seems a little messy, you can extract all you interceptor logic out into a separate class and use the `WebService#require` method to get an instance of that interceptor with all its dependencies injected.

Modifying our `AuthInterceptor` from earlier, we get:

```java
public class AuthInterceptor implements Consumer<HttpContext> {
    
    private UserDAO dao;
    
    @Inject
    public AuthInterceptor(UserDAO dao) {
        this.dao = dao;
    }
    
    @Override
    public void accept(HttpContext ctx) {
        Cookie tokenCookie = ctx.getCookie("authToken");
        if(tokenCookie == null) ctx.response().unauthorized("You must log in.");
        else {
            ctx.put("user", dao.find(tokenCookie.getValue()));
            ctx.next();
        }
    }
}
```

And in our web service, we create an instance of it using `require()`:

```java
public void addInterceptors(InterceptorCollection interceptors) {
  interceptors.addInterceptor(require(AuthInterceptor.class));
}
```

## Managing configuration

Bolt comes with an `Env` class which provides convenience methods for reading basic primitives stored in environment variables. Use the `getString()`, `getBoolean()` and `getInt()` methods to retrieve String, Boolean and Integer properties accordingly.

Environment variables are read in the following order:

1. Java system properties (`-D`)
2. System environment variables defined in the OS

#### Am I in Dev or Prod mode?

The `app.mode` environment/system property can be used to determine the mode the application is running in. Use the shortcuts `Env.isDev()` and `Env.isProd()` which will return Boolean values. E.g.:

```java
if(Env.isProd()) useProductionDatabase();
else useDevelopmentDatabase();
```

For the shortcut methods to work, the `app.mode` value should be one of "development" or "production", however you are free to use other values if you like to reflect your current application environment. In that case, you will have to use the `Env.getString("app.mode")` method.

## Testing

Bolt was designed from the ground up to facilitate easy testing. Controllers in Bolt can be tested pretty much the same way you would test your other domain objects, but Bolt also provides some handy helper classes for spinning up a real application server and sending web requests as part of your testing workflow.

### The TestApplicationServer

Bolt provides an application server that you can use to test your controllers. You can send real HTTP requests to this server with the aid of the `com.boltframework.utils.httpclient.HttpRequest` class. 

The `com.boltframework.test.TestApplicationServer` requires an instance of your controller and can accept an optional Web Service definition and a dependency module.

### A basic controller test

Let's look at how we can use the `TestApplicationServer` and the `HttpRequest` to:

1. Spin up a test server
2. Send a request
3. Ensure we receive an `OK` response
4. Terminate the server

Let's start with the controller:

```java
@RequestMapping("test")
public class TestController extends Controller {
    
    @Get("greet")
    public void greet() {
        response().ok("Hello, world!");
    }
}
```

Now, let's write the test for the `/greet` route:

```java
import static orr.junit.Assert.*;
import static com.boltframework.utils.httpclient.HttpRequest.get;
import static org.hamcrest.Matchers.containsString;
// Other imports

public class TestControllerTest {
    
    @ClassRule
    static TestApplicationServer server = new TestApplicationServer(new TestController());
    
    @Test
    public void greetingTest() {
        server.createRequest(get("/test/greet")).then(response -> {
            assertEquals(200, response.getStatus());
            assertThat(response.getBody(), containsString("Hello, world!"));
        });
    }
    
    @AfterClass
    public void stopServer() {
        server.stop();
    }
}

```

If you look at the above code, you'll realize that you don't have to manually start the server. If the server isn't running when you make your first request, it will spin up the server and it will stay running for the duration of all your test cases. Calling `server.stop()` isn't necessary since it will exit after all tests have completed.

And that's how easy it is to create a test for your controller!

### The HttpRequest

:warning: _This feature is not on par with what you'd find in Apache HttpClient, but it'll get the job done, mostly._

The `com.boltframework.utils.httpclient.HttpRequest` class is what you use to build HTTP requests to send to your controllers during testing. This class provides some handy methods for you to create anything from a simple GET request, to a POST request with a JSON body and custom headers and cookies.

Here's a breakdown of the methods available to build a request:

```java
/* Create a DELETE request */
static HttpRequest delete(String path);

/* Create a GET request */
static HttpRequest get(String path);

/* Create a PATCH request */
static HttpRequest patch(String path);

/* Create a POST request */
static HttpRequest post(String path);

/* Create a PUT request */
static HttpRequest put(String path);

/* Set the Accept header */
HttpRequest accept(String contentType);

/* Set the request body */
HttpRequest body(String body);

/* Set the Content-Type header */
HttpRequest contentType(String contentType);

/* Add a cookie */
HttpRequest cookie(String name, String value);

/* Add a cookie */
HttpRequest cookie(com.boltframework.utils.httpclient.HttpEntity.Cookie cookie);

/* Instruct the request to follow redirects. Default: false */
HttpRequest followRedirects();

/* Add a header */
HttpRequest header(String key, Object value);

/* Set Content-Type: application/json and a JSON request body */
HttpRequest json(String body);

/* Set Content-Type: application/json and a JSON request body using basic JSON serialization */
HttpRequest json(Object object);

/* Set the request method */
HttpRequest method(String method);

/* Set the path of the request e.g. /home */
HttpRequest path(String path);

/* Add to the queryString of your request path */
HttpRequest query(String key, Object value);
```

### The HttpResponse

The `com.boltframework.utils.httpclient.HttpResponse` class allows you to examine certain key features of an HTTP response.

```java
/* Get the body of the response */
String getBody();

/* Get any cookies contained in the response */
HashMap<String, Cookie> getCookies();

/* Get a response header */
String getHeader(String header);

/* Get the path of the response */
String getPath();

/* Get the response status code */
Integer getStatus();

/* Get the full URL of the response */
String getUrl();
```