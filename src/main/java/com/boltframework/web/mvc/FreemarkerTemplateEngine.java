package com.boltframework.web.mvc;

import com.boltframework.utils.Env;
import freemarker.template.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * A TemplateEngine implementation using the Freemarker template engine. Used by default.
 */
public class FreemarkerTemplateEngine extends TemplateEngine {
  private Configuration configuration;

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public String render(String template, Map<String, Object> data) {
    try {
      String templateName = template.matches(".*(\\."+defaultExtension+")$") ? template : template + defaultExtension;
      Template _template = configuration.getTemplate(templateName);
      Writer out = new StringWriter();
      _template.process(data, out);
      out.close();
      return out.toString();
    } catch(Exception e) {
      return e.getMessage();
    }
  }

  public void build() {
    try {
      Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
      configuration.setDirectoryForTemplateLoading(new File(templatePath));
      configuration.setDefaultEncoding("UTF-8");
      if(Env.isDev()){
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        configuration.setSetting(Configuration.TEMPLATE_UPDATE_DELAY_KEY, "0s");
      }
      else {
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setSetting(Configuration.TEMPLATE_UPDATE_DELAY_KEY, "3600s");
      }
      configuration.setLogTemplateExceptions(false);
      configuration.setWrapUncheckedExceptions(true);
      this.configuration = configuration;
    } catch(IOException | TemplateException e) {
      e.printStackTrace();
    }
  }
}
