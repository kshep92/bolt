package com.boltframework.config;

public class HttpConfiguration {
  private String host ;
  private Integer port;
  private String scheme;

  HttpConfiguration() {
    host = "localhost";
    port = 3000;
    scheme = "http";
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public HttpConfiguration host(String host) {
    setHost(host);
    return this;
  }

  public HttpConfiguration port(Integer port) {
    setPort(port);
    return this;
  }

  public HttpConfiguration scheme(String scheme) {
    setScheme(scheme);
    return this;
  }

  public String getEndpointUrl() {
    return String.format("%s://%s:%d", scheme, host, port);
  }
}
