package app.controllers

import com.boltframework.web.mvc.Controller
import com.boltframework.web.routing.annotations.Delete
import com.boltframework.web.routing.annotations.Get
import com.boltframework.web.routing.annotations.Post
import com.boltframework.web.routing.annotations.RequestMapping
import io.vertx.ext.web.Cookie

@RequestMapping('context')
class HttpContextController extends Controller {

  @Post("add-cookie")
  public void addCookie() {
    context().addCookie('foo', 'bar').response.redirect('context/get-cookie')
  }
  @Get("get-cookie")
  public void getCookie() {
    Cookie cookie = context().getCookie('foo')
    if(cookie == null) response().notFound('Could not get cookie [foo]')
    else response().ok(cookie.value)
  }

  @Delete("remove-cookie")
  public void removeCookie() {
    context().removeCookie('foo').response.ok('done')
  }

  @Post("get-body-as")
  public void getBodyAs() {
    def form = context().getBodyAs(LoginForm)
    response().ok().json(form)
  }

  @Get("get-path-param/:id")
  public void getPathParam() {
    Integer id = context().getPathParam('id', Integer)
    response().ok((id + 10).toString())
  }

  public static class LoginForm {
    String username
    String password
  }

}
