package app

import AbstractModule
import Provides

class Configuration extends AbstractModule {

  @SuppressWarnings("GrMethodMayBeStatic")
  @Provides
  public UserController getUserController() {
    return new UserController(new UserService())
  }

}
