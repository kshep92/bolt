package com.boltframework.test

import com.boltframework.utils.Env
import org.junit.Test
import static org.junit.Assert.*

class EnvTest {

  @Test
  public void 'get string environment variable'() {
    String prop = 'app.mode'
    System.setProperty(prop, 'production')
    assertNotNull(Env.getString(prop))
    assertEquals('production', Env.getString(prop))
    System.clearProperty(prop)
    assertNull(Env.getString(prop))
  }

  @Test
  public void 'get integer environment variable'() {
    String prop = 'app.port'
    System.setProperty(prop, '3000')
    assertNotNull(Env.getInt(prop))
    assertEquals(3000, Env.getInt(prop))
    System.clearProperty(prop)
    assertNull(Env.getInt(prop))
  }

  @Test
  public void 'get boolean value'() {
    String prop = 'db.show-sql'
    System.setProperty(prop, 'true')
    assertNotNull(Env.getBoolean(prop))
    assertTrue(Env.getBoolean(prop))
    System.setProperty(prop, 'false')
    assertFalse(Env.getBoolean(prop))
    System.clearProperty(prop)
    assertFalse(Env.getBoolean(prop))
  }
}
