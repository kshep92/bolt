package com.boltframework.web.mvc;

import com.boltframework.utils.Env;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * The default TemplateEngine implementation for Bolt -- Freemarker.
 */
public class FreemarkerTemplateEngine extends TemplateEngine {
  private Configuration configuration;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  public FreemarkerTemplateEngine() {
    defaultExtension = ".ftl";
  }

  @Override
  public String render(String template, Map<String, Object> data) {
    try {
      String templateName = getTemplateName(template);
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
    } catch(FileNotFoundException e) {
      logger.warn(e.getMessage());
    } catch(IOException e) {
      logger.error(e.getMessage());
    } catch (TemplateException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
