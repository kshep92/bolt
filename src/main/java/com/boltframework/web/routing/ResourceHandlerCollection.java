package com.boltframework.web.routing;


public class ResourceHandlerCollection {

  public ResourceHandlerProperties addHandler(String path, String webRoot) {
    ResourceHandlerProperties properties = new ResourceHandlerProperties(path, webRoot);
    return PropertiesRegistry.addResourceHandlerProperties(properties);
  }


}
