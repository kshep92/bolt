package app.controllers;

import com.boltframework.web.mvc.Controller;
import com.boltframework.web.routing.annotations.RequestMapping;
import com.boltframework.web.routing.annotations.Route;

@RequestMapping("/accounts")
public class Accounts extends Controller {

  @Route("/")
  public void index() {
    response().ok("You're in the Accounts register");
  }
}
