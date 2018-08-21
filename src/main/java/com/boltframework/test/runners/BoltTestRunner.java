package com.boltframework.test.runners;

import com.boltframework.BoltApplication;
import com.boltframework.config.ContextConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import com.boltframework.test.BoltApplicationTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "WeakerAccess"})
public class BoltTestRunner extends BlockJUnit4ClassRunner {

  private Class<? extends AbstractModule>[] configurationClasses;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public BoltTestRunner(Class<? extends BoltApplicationTest> klass) throws InitializationError {
    super(klass);
    initialize(klass);
  }

  private void initialize(Class<?> testClass) {
    setContextConfiguration(testClass);
    if (testClass.getSuperclass() == BoltApplicationTest.class) {
      BoltApplication application = Guice.createInjector(getConfigurationModules()).getInstance(getApplicationType(testClass));
      BoltApplicationTest.setApplication(application);
      return;
    }
    initialize(testClass.getSuperclass());
  }

  private void setContextConfiguration(Class<?> c) {
    ContextConfiguration contextConfiguration = c.getAnnotation(ContextConfiguration.class);
    if (contextConfiguration != null && configurationClasses == null)
      configurationClasses = contextConfiguration.value();
  }

  private Set<? extends AbstractModule> getConfigurationModules() {
    Set result = new HashSet();
    if (configurationClasses == null) return result;
    Arrays.asList(configurationClasses).forEach(_class -> {
      try {
        result.add(_class.newInstance());
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    });
    return result;
  }

  @SuppressWarnings("unchecked")
  private <T extends BoltApplication> Class<T> getApplicationType(Class<?> testClass) {
    ParameterizedType parameterizedType = (ParameterizedType) testClass.getGenericSuperclass();
    return (Class<T>) parameterizedType.getActualTypeArguments()[0];
  }

}
