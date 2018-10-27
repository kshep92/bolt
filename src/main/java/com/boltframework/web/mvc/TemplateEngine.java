package com.boltframework.web.mvc;

import java.util.HashMap;
import java.util.Map;

/**
 * Bolt currently using Pebble as a template engine by choice, but in the future I would like users to be able to choose
 * their own engines. This is what that interface would look like.
 */
public interface TemplateEngine {
  String render(String template, Map<String, Object> data);
  default String render(String template) { return render(template, new HashMap<>()); }
}
