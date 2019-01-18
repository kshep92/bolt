package com.boltframework.data.forms;

import com.boltframework.context.ApplicationContext;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Form {
  private Map<String, String> errors = new HashMap<>();
  private String errorMessage;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Map<String, String> getErrors() {
    return errors;
  }

  /**
   * Get the entire result of all validation. Returns a Map of the main error message and any error messages for
   * form fields
   * @return A {@link Map} of all the errors on the form.
   */
  public Map getValidationResult() {
    Map<String, Object> errors = new HashMap<>();
    errors.put("errorMessage", errorMessage);
    errors.put("errors", this.errors);
    return errors;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String validate() {
    return null;
  }

  private void checkConstraintValidations() {
    Validator validator = Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory().getValidator();
    Set<ConstraintViolation<Form>> violations = validator.validate(this);
    for(ConstraintViolation violation : violations) {
      String field = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.put(field, message);
    }
  }

  public Boolean valid() {
    checkConstraintValidations();
    if(!errors.isEmpty()) {
      errorMessage = "Invalid data submitted.";
      return false;
    }
    errorMessage = validate();
    return errorMessage == null;
  }

  public Boolean copyPropertiesTo(Object target) {
    return map(this, target, true);
  }

  public Boolean copyPropertiesTo(Object target, Boolean ignoreNulls) {
    return map(this, target, ignoreNulls);
  }

  public Boolean copyPropertiesFrom(Object source) {
    return map(source, this, true);
  }
  public Boolean copyPropertiesFrom(Object source, Boolean ignoreNulls) {
    return map(source, this, ignoreNulls);
  }

  public <T> T getInstanceOf(Class<T> tClass) {
    T instance = ApplicationContext.getBean(tClass);
    map(this, instance, true);
    return instance;
  }

  private Boolean map(Object source, Object target, Boolean ignoreNulls) {
    try {

      Class sourceCLass = source.getClass();
      Class targetClass = target.getClass();

      /* Build a map of our source class' property names and their descriptors [name: descriptor]  */

      /* Declare the map */
      Map<String, PropertyDescriptor> sourceDescriptors = new HashMap<>();

      /* Ignore certain class properties */
      Set<String> ignoredProperties = new HashSet<>();
      ignoredProperties.add("class");
      ignoredProperties.add("errors");
      ignoredProperties.add("errorMessage");
      ignoredProperties.add("metaClass"); // For groovy

      /* Build the sourceDescriptors map */
      for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(sourceCLass).getPropertyDescriptors()) {
        String displayName = propertyDescriptor.getDisplayName();
        if (!ignoredProperties.contains(displayName))
          sourceDescriptors.put(propertyDescriptor.getDisplayName(), propertyDescriptor);
      }

      /* Loop through all the properties on the target class */
      for (PropertyDescriptor targetDescriptor : Introspector.getBeanInfo(targetClass).getPropertyDescriptors()) {

        /* Check if our source class has this property */
        PropertyDescriptor sourceDescriptor = sourceDescriptors.get(targetDescriptor.getDisplayName());
        if (sourceDescriptor != null) {
          /* If our source class has this property, then copy it to the target instance*/
          Method writeMethod = targetDescriptor.getWriteMethod();
          assert writeMethod != null;
          Method readMethod = sourceDescriptor.getReadMethod();
          assert readMethod != null;
          Object value = readMethod.invoke(source);
          if(value == null && ignoreNulls) continue;
          writeMethod.invoke(target, value);
        }
      }
      return true;
    }
    catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationError e) {
      e.printStackTrace();
      return false;
    }
  }
}
