package com.boltframework.old;

public class StaticFilesConfiguration {
  private Boolean cacheEnabled = false;
  private Long maxAge = 0L;

  public Boolean getCacheEnabled() {
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
}
