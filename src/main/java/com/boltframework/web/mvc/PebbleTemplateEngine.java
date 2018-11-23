package com.boltframework.web.mvc;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Will probably replace the Freemarker engine soon. It's supports template inheritance and is extendable.
 */
public class PebbleTemplateEngine extends TemplateEngine {

  private PebbleEngine pebbleEngine;

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

  public void build() {
    PebbleEngine.Builder builder = new PebbleEngine.Builder();
    // Set properties
    pebbleEngine = builder.build();
  }
}
