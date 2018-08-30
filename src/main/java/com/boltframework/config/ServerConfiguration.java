package com.boltframework.config;

public class ServerConfiguration {

  private HttpConfiguration httpConfiguration = new HttpConfiguration();

  public ServerConfiguration() {}

  public HttpConfiguration getHttpConfiguration() {
    return httpConfiguration;
  }

  public HttpConfiguration http() {
    return httpConfiguration;
  }

  public class HttpConfiguration {
    private String host = "localhost";
    private Integer port = 3000;
    private String scheme = "http";

    HttpConfiguration() {}

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
}
