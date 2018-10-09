package com.boltframework.test

import groovy.transform.ToString
import org.junit.Test
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import static org.junit.Assert.*

/**
 * Toying around with the idea of storing route access rules in a permissions.xml file
 */
class PermissionsTest {

  @Test
  public void 'read permissions file'() {
    def file = new File('permissions.xml')
    assert file.exists()
    List<Rule> rules = new ArrayList<>()
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    Element $root = documentBuilder.parse(file).documentElement
    assertNotNull($root)
    NodeList $rules = $root.getElementsByTagName("rule")
    assertTrue($rules.length > 0)
    for(int count = 0; count < $rules.length; count++) {
      Node $node = $rules.item(count)
      if($node.nodeType == Node.ELEMENT_NODE) {
        Element $rule = (Element) $node
        Rule rule = new Rule(
            $rule.getAttribute('path'),
            $rule.getAttribute('pattern'),
            $rule.getAttribute('permissions')
        )
        rules.add(rule)
      }
    }
    println(rules)
  }

  @ToString(includeNames = true, includePackage = false)
  class Rule {
    String path
    String pattern
    Set<String> permissions

    Rule(String path, String pattern, String permissions) {
      setPath(path)
      setPattern(pattern)
      setPermissions(permissions)
    }

    void setPermissions(String permissions) {
      this.permissions = new HashSet<>(Arrays.asList(permissions.split('\\s')))
    }

    void setPath(String path) {
      this.path = path
    }

    void setPattern(String pattern) {
      this.pattern = pattern
    }
  }
}
