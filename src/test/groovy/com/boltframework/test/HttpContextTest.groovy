package com.boltframework.test

import com.google.common.net.MediaType
import org.junit.Test
import org.slf4j.LoggerFactory

import static org.junit.Assert.*

class HttpContextTest {

  String Accept = 'text/plain; text/html; application/json'
  String ContentType = MediaType.FORM_DATA.toString()

  @Test
  void acceptRegexTest() {
    assertTrue('application/*', accepts('application/*'))
    assertTrue('*/json', accepts('*/json'))
    assertTrue('text/*', accepts('text/*'))
    assertTrue('text/plain', accepts('text/plain'))
    assertTrue('text/html', accepts('text/html'))
    assertFalse('application/pdf', accepts('application/pdf'))
  }

  @Test
  void contentTypeTest() {
    assertTrue(contentTypeMatches('form'))
    assertFalse(contentTypeMatches('json'))
  }
// Check what the requester will accept
  Boolean accepts(String contentType) {
    return Accept.matches(convertToRegex(contentType))
  }

  // Check the content type of the request
  Boolean contentTypeMatches(String contentType) {
    return ContentType.matches(convertToRegex(contentType))
  }

  static String convertToRegex(String glob) {
    if(glob.charAt(0) == '*' as Character)
      glob = glob.substring(1)
    if(glob.charAt(glob.length() - 1) == '*')
      glob = glob.substring(0, glob.length() - 1)
    StringBuilder sb = new StringBuilder('*').append(glob).append('*')
    LoggerFactory.getLogger('HttpContextTest#convertToRegex').debug(sb.toString())
    return sb.toString().replace('*', ".*")
  }
}
