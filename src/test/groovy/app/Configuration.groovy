package app

import com.google.inject.AbstractModule
import com.google.inject.Provides

class Configuration extends AbstractModule {

  @SuppressWarnings("GrMethodMayBeStatic")
  @Provides
  public UserController getUserController() {
    return new UserController(new UserService())
  }

}
