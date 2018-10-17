package app.controllers;

import com.boltframework.utils.Env;
import com.boltframework.web.HttpResponse;
import com.boltframework.web.mvc.Controller;
import com.boltframework.web.routing.annotations.*;
import com.google.inject.Inject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;

import java.util.Arrays;

@SuppressWarnings("unused")
@RequestMapping
public class Application extends Controller {

  private Vertx vertx;

  @Inject
  public Application(Vertx vertx) {
    this.vertx = vertx;
  }

  @Get
  public void index() {
    response().ok("index");
  }

  @Post("post")
  public void post() {
    String body = context().getBodyAsString();
    response().ok(body);
  }

  @Put("put")
  public void put() {
    String body = context().getBodyAsString();
    response().ok(body);
  }

  @Delete("delete")
  public void delete() {
    response().ok("delete");
  }

  @Get("cookie")
  public void cookies() {
    context().addCookie("foo", "bar").getResponse().ok().end();
  }

  @Route(value = "/cookie", method = HttpMethod.POST)
  public void addCookie() {
    response().ok(context().getCookie("foo").getValue());
  }

  @Get("ip")
  public void ip() {
    logger.debug("Running the ip() method first...");
    context().put("message", "Hey!").next();
  }

  @Route(value = "/ip", order = 1)
  public void ipAddress() {
    response().ok("IP address is: " + request().getIpAddress());
  }

  @Route("/nonblocking")
  public void nonblocking() {
    context().put("Key", "value").reroute("/blocking");
  }

  @Route(value = "/blocking/:user", blocking = true)
  public void blocking() {
    try {
      String threadName = Thread.currentThread().getName();
      String user = context().getPathParam("user");
      logger.debug("Blocking thread {}", threadName);
      Thread.sleep(5000);
      response().ok("Talking to "+ user +" from " + threadName);
      logger.debug("Released {}", threadName);
      logger.debug("Context is null? {}", context() == null);
    } catch (InterruptedException ignored) {}
  }

  @Route("sendfile")
  public void sendFile() {
    response().sendFile("web/public/index.html");
  }

  @Route("stream")
  public void stream() {
    String[] content = new String[] { "This\n", "is\n", "a\n", "file\n" };
    HttpResponse response = response()
        .setChunked(true)
        .addHeader("Content-Disposition", "attachment; filename=stream.txt")
        .addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
    Arrays.stream(content).forEach(response::write);
    response.setStatusCode(HttpResponseStatus.OK).end();
  }

  @Route("test")
  public void test() {
    if(Env.isDev()) response().send("We're in development");
    else response().send("We're in production. Be serious.");
  }

  @Route("shutdown")
  public void shutdown() {
    response().ok("Shutting down the application. Bye!");
    vertx.setTimer(200, (delay) -> System.exit(200));
  }

  @Get("todo")
  public void todo() {
    response().todo();
  }
}
