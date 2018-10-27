package com.boltframework.utils.httpclient;

/**
 * A class that wraps the data returned from a typical {@link java.net.HttpURLConnection} transaction. Used in
 * {@link HttpClient}
 * Headers, cookies, response statuses, response body, etc.
 */
public class HttpResponse extends HttpEntity {
  private Integer status;
  private String url;

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
}
