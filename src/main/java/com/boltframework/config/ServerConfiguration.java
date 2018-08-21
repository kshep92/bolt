package com.boltframework.config;

public class ServerConfiguration {

  private HttpConfiguration httpConfiguration = new HttpConfiguration();
  private StaticFilesConfiguration staticFilesConfiguration = new StaticFilesConfiguration();

  public ServerConfiguration() {}

  public HttpConfiguration getHttpConfiguration() {
    return httpConfiguration;
  }

  public HttpConfiguration http() {
    return httpConfiguration;
  }

  public StaticFilesConfiguration getStaticFilesConfiguration() {
    return staticFilesConfiguration;
  }

  public StaticFilesConfiguration resources() {
    return staticFilesConfiguration;
  }

  public class StaticFilesConfiguration {
    private String url = "/public";
    private String dir = "web/public";
    private Boolean cacheEnabled = false;
    private Long maxAge = 0L;

    StaticFilesConfiguration() {
      //TODO: Set up caching automatically for Production
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getDir() {
      return dir;
    }

    public void setDir(String dir) {
      this.dir = dir;
    }

    public Boolean isCacheEnabled() {
      return cacheEnabled;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
      this.cacheEnabled = cacheEnabled;
    }

    public Long getMaxAge() {
      return maxAge;
    }

    public void setMaxAge(Long maxAge) {
      this.maxAge = maxAge;
    }

    public StaticFilesConfiguration url(String url) {
      setUrl(url);
      return this;
    }

    public StaticFilesConfiguration dir(String dir) {
      setUrl(dir);
      return this;
    }

    public StaticFilesConfiguration cache(Boolean enabled) {
      setCacheEnabled(enabled);
      return this;
    }

    public StaticFilesConfiguration maxAge(long maxAge) {
      setMaxAge(maxAge);
      return this;
    }
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
