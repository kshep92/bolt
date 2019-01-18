package com.boltframework.test.forms

import com.boltframework.data.forms.Form
import com.boltframework.utils.Json
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

import static org.junit.Assert.*

class FormTest {

  SignupForm form
  Logger logger = LoggerFactory.getLogger(getClass())

  @Before
  public void setUp() throws Exception {
    form = new SignupForm()
  }

  @Test
  public void 'constraint validation'() {
    assertFalse(form.valid())
    assertFalse(form.errors.isEmpty())
    assertEquals(2, form.errors.size(),)
    logger.debug('{}', form.errors)
    assertNotNull(form.validationResult['errors']['password'])
    assertEquals('must not be blank', form.validationResult['errors']['password'])
    logger.debug(form.errorMessage)
  }

  @Test
  public void 'custom validate method'() {
    form.username = 'shibby'
    form.password = 'password123'
    assertFalse(form.valid())
    assertTrue(form.errors.isEmpty())
    assertEquals("We don't like that guy", form.errorMessage)
    form.username = 'jdoe'
    assertTrue(form.valid())
  }

  @Test
  public void 'can copy properties to domain model'() {
    def account = new Account()
    form.username = 'jdoe'
    form.password = 'password123'
    assertTrue(form.copyPropertiesTo(account))
    assertEquals(form.username, account.username)
    assertEquals(form.password, account.password)
  }

  @Test
  public void 'can copy properties to an instance class'() {
    form.username = 'jdoe'
    form.password = 'password123'
    def account = form.getInstanceOf(Account)
    assertNotNull(account)
    assertEquals(form.username, account.username)
    assertEquals(form.password, account.password)
    logger.debug('{}', account.createdAt)
  }

  @Test
  public void 'get validation result'() {
    form.valid()
    assertNotNull(form.validationResult)
    assertNotNull(form.validationResult['errorMessage'])
    assertNotNull(form.validationResult['errors'])
    assertEquals(form.errorMessage, form.validationResult['errorMessage'])
    assertEquals(form.errors.size(), ((Map)form.validationResult['errors']).size())
  }

  public class SignupForm extends Form {
    @NotBlank
    String username
    @NotBlank
    @Size(min = 8)
    String password

    @Override
    String validate() {
      if(username == 'shibby') return "We don't like that guy"
      return null
    }
  }

  public static class Account {
    String username
    String password
    Date createdAt = new Date()
  }
}
