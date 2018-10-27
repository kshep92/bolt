package com.boltframework.web.routing;

public class ResourceHandlerProperties extends AbstractRouteProperties {
  private String path;
  private String webRoot;
  private Long maxAge;
  private Boolean cache;

  ResourceHandlerProperties(String path, String webRoot) {
    this.path = formatPath(path);
    this.webRoot = webRoot;
    cache = false;
    maxAge = 0L;
  }

  public ResourceHandlerProperties maxAge(long maxAge) {
    this.maxAge = maxAge;
    return this;
  }

  public ResourceHandlerProperties cache(Boolean enableCache) {
    this.cache = enableCache;
    return this;
  }

  String getPath() {
    return path;
  }

  String getWebRoot() {
    return webRoot;
  }

  Long getMaxAge() {
    return maxAge;
  }

  Boolean getCache() {
    return cache;
  }
}
