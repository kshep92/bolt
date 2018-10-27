package com.boltframework.web.mvc;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * The default TemplateEngine implementation for Bolt -- Pebble.
 */
public class PebbleTemplateEngine implements TemplateEngine {

  private PebbleEngine pebbleEngine = pebbleEngine();

  @Override
  public String render(String template, Map<String, Object> data) {
    try {
      Writer writer = new StringWriter();
      PebbleTemplate pebbleTemplate = pebbleEngine.getTemplate(template);
      pebbleTemplate.evaluate(writer, data);
      return writer.toString();
    } catch (PebbleException | IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private PebbleEngine pebbleEngine() {
    PebbleEngine.Builder builder = new PebbleEngine.Builder();
    // Set properties
    return builder.build();
  }
}
