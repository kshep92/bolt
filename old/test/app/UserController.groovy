package app

import com.boltframework.old.RouteBuilder
import com.boltframework.web.Controller
import Inject

class UserController implements Controller {

  UserService userService

  @Inject
  UserController(UserService userService) {
    this.userService = userService
  }

  public String createUser() {
    return userService.save()
  }

  public String updateUser() {
    return userService.update()
  }

  @Override
  void doRoutes(RouteBuilder routes) {
    routes.get({ it.response.send(userService.save()) })
    routes.get('/show/:id', { ctx -> ctx.response.send(ctx.getPathParam('id')) })
  }
}
