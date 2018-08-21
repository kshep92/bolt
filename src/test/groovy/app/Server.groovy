package app

import com.google.inject.Inject

class Server {

  UserController controller
  HashMap<String, Closure> routes = new HashMap<>()

  @Inject
  Server(UserController controller) {
    this.controller = controller
    routes.put('/users/create', { controller.createUser() })
    routes.put('/users/update', { controller.updateUser() })
  }

  public void handleRequest(String path) {
    println(routes.get(path).call())
  }
}
