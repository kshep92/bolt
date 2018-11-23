package com.boltframework.web.mvc;

import java.util.HashMap;
import java.util.Map;

/**
 * Bolt currently using Pebble as a template engine by choice, but in the future I would like users to be able to choose
 * their own engines. This is what that interface would look like.
 */
public abstract class TemplateEngine {
  String templatePath = "www/views";
  String defaultExtension = ".html";

  public void setTemplatePath(String templatePath) {
    this.templatePath = templatePath;
  }

  public TemplateEngine templatePath(String templatePath) {
    setTemplatePath(templatePath);
    return this;
  }

  public void setDefaultExtension(String defaultExtension) {
    this.defaultExtension = defaultExtension.matches("^\\.[a-z]+") ? defaultExtension : "." + defaultExtension;
  }

  public TemplateEngine defaultExtension(String defaultExtension) {
    setDefaultExtension(defaultExtension);
    return this;
  }

  /**
   * Utility method for concatenating the template name and the default extension.
   * @param template The name of the template file
   * @return The template file + extension (if none was specified)
   */
  public String getTemplateName(String template) {
    return template.matches(".*(\\."+defaultExtension+")$") ? template : template + defaultExtension;
  }

  public abstract String render(String template, Map<String, Object> data);

  /**
   * Most of the template engines I've used seem to require a configuration object to be built first
   * before use. This is where users could build that configuration object.
   *
   * This method will be called by Bolt when setting up dependencies.
   */
  public abstract void build();

  public String render(String template) {
    return render(template, new HashMap<>());
  }
}
