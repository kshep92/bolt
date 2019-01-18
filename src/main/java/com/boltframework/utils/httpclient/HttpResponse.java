package com.boltframework.utils.httpclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that wraps the data returned from a typical {@link java.net.HttpURLConnection} transaction. Used in
 * {@link HttpClient}
 * Headers, cookies, response statuses, response body, etc.
 */
public class HttpResponse extends HttpEntity {
  private Integer status;
  private String url;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    String template = "# HttpResponse: %s\nStatus=%d\n%s\n";
    return String.format(template, url, status, super.toString());
  }

  public void printBody() {
    logger.debug(getBody());
  }

  public Boolean isOk() {
    return status >= 200 && status <= 300;
  }

  public Boolean isRedirect() {
    return status >= 301 && status <= 307;
  }

  public Boolean isBadRequest() {
    return status == 400;
  }

  public Boolean isUnauthorized() {
    return status == 401;
  }

  public Boolean isError() {
    return status >= 500;
  }
}
