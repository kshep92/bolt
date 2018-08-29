package com.boltframework.test

import app.Configuration
import app.UserController
import com.boltframework.test.utils.Registry
import org.junit.Test

import static org.junit.Assert.*

class RegistryTest {

  @Test
  void getInstanceTest() {
    UserController controller = Registry.from(Configuration).getInstance(UserController)
    assertNotNull(controller)
  }
}
