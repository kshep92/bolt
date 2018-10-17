package app.controllers;

import com.boltframework.web.mvc.Controller;
import com.boltframework.web.routing.annotations.RequestMapping;
import com.boltframework.web.routing.annotations.Route;
import io.vertx.core.http.HttpMethod;

import java.util.HashMap;

@RequestMapping("users")
public class Users extends Controller {

  public void setUser() {
    Long id = Long.parseLong(request().getParam("id"));
    context().put("user", new HashMap<>()).next();
  }

  @Route
  public void index() {
    response().ok("You're in the users controller now!");
  }

  @Route("cookie")
  public void cookie() {
    response().ok("According to the cookie, your name is: " + context().getCookie("Name").getValue());
  }

  @Route(method = HttpMethod.POST)
  public void create() {
    response().ok("We got the POST");
  }

}
