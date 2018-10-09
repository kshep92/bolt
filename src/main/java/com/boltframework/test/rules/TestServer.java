package com.boltframework.test.rules;

import com.boltframework.Bolt;
import com.boltframework.ReadyState;
import com.boltframework.utils.httpclient.HttpClient;
import com.boltframework.utils.httpclient.HttpRequest;
import com.boltframework.web.WebService;
import com.google.inject.Module;
import org.junit.rules.ExternalResource;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//TODO: Make this the new way to spin up test servers
public class TestServer extends ExternalResource {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private HttpClient httpClient = new HttpClient();
  private Class<? extends WebService> webServiceClass;
  private Set<Module> configurationModules = new HashSet<>();
  private Bolt application;
  private int port;

  public TestServer(Class<? extends WebService> serviceClass) {
    webServiceClass = serviceClass;
  }

  public TestServer withConfiguration(Module... modules) {
    configurationModules.addAll(Arrays.asList(modules));
    return this;
  }

  public HttpClient createRequest(HttpRequest request) {
    return httpClient.createRequest(request);
  }

  @Override
  protected void before() throws Throwable {
    super.before();
    application = Bolt.createService(webServiceClass).withConfiguration(configurationModules);
    port = new Random().nextInt(3000) + 3000;
    application.start(port);
    while(application.getReadyState() != ReadyState.Running) Thread.sleep(300);
    if(application.getReadyState() == ReadyState.Error)
      throw new InitializationError("Could not start the server.");
    else
      httpClient.setUrl("http://localhost:"+port);
  }

  @Override
  protected void after() {
    super.after();
    logger.info("Stopping application server...");
    application.stop();
  }
}
