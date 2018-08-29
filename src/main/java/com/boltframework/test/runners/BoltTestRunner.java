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
import java.util.List;
import java.util.Set;

@SuppressWarnings({"unchecked", "WeakerAccess"})
public class BoltTestRunner extends BlockJUnit4ClassRunner {


  private static Class<? extends AbstractModule>[] currentConfigurationClasses = new Class[0];
  private Logger logger = LoggerFactory.getLogger(getClass());

  public BoltTestRunner(Class<? extends BoltApplicationTest> klass) throws InitializationError {
    super(klass);
    initialize(klass);
  }

  private void initialize(Class<?> testClass) {
    if (testClass.getSuperclass() == BoltApplicationTest.class) {
      logger.debug("CLASS UNDER TEST -- {}", testClass);
      Class<? extends AbstractModule>[] newConfigurationClasses = getContextConfigurationClasses(testClass);
      Class<? extends BoltApplication> applicationType = getApplicationType(testClass);
      Set<? extends AbstractModule> configurationModules = getConfigurationModules(newConfigurationClasses);
      if(BoltApplicationTest._getRunningApp() == null) {
        BoltApplication application = Guice
            .createInjector(configurationModules)
            .getInstance(applicationType);
        BoltApplicationTest.setApp(application);
      }
      return;
    }
    initialize(testClass.getSuperclass());
  }

  private Class<? extends AbstractModule>[] getContextConfigurationClasses(Class<?> testClass) {
    Class[] result = new Class[0];
    ContextConfiguration contextConfiguration = testClass.getAnnotation(ContextConfiguration.class);
    if(contextConfiguration != null) {
      result = contextConfiguration.value();
    }
    return result;
  }

  private Boolean configurationsAreEqual(Class[] a, Class[] b) {
    if(a.length != b.length) return false;
    Boolean result = true;
    List<Class> bList = Arrays.asList(b);
    for (Class entry : a) {
      if(!bList.contains(entry)) {
        result = false;
        break;
      }
    }
    return result;
  }

  private Set<? extends AbstractModule> getConfigurationModules(Class<? extends AbstractModule>[] classes) {
    Set result = new HashSet();
    if (classes.length == 0) return result;
    Arrays.asList(classes).forEach(_class -> {
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
