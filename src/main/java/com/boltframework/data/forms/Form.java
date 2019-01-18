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

/**
 * Utility class to handle validation of user submitted data. It also has data transfer functionality.
 *
 * @author Kevin Sheppard
 */
public abstract class Form {
  private Map<String, String> errors = new HashMap<>();
  private String errorMessage;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Map<String, String> getErrors() {
    return errors;
  }

  /**
   * Get the entire result of all validation checks. Returns a Map of the main error message and any error messages for
   * form fields.
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

  /**
   * A method that can be overridden to implement custom validation logic. The value returned will be set as the main
   * error message that {@link #getErrorMessage()} will return.
   * @return A null value if validation is successful or an error message if not.
   */
  public String validate() {
    return null;
  }

  /**
   * Checks annotation based constraints violations.
   */
  private void checkConstraintValidations() {
    Validator validator = Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory().getValidator();
    Set<ConstraintViolation<Form>> violations = validator.validate(this);
    for(ConstraintViolation violation : violations) {
      String field = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.put(field, message);
    }
  }

  /**
   * Checks the validity of the data submitted.
   * @return true if there are no constraint violations and the {@link #validate()} method returns a null String.
   */
  public Boolean valid() {
    checkConstraintValidations();
    if(!errors.isEmpty()) {
      errorMessage = "Invalid data submitted.";
      return false;
    }
    errorMessage = validate();
    return errorMessage == null;
  }

  /**
   * Copies the properties of the form to an object. It ignores null values by default.
   * Delegates to {@link #map(Object, Object, Boolean)}
   * @param target The object to receive this form's values.
   * @return true if the mapping was successful.
   */
  public Boolean copyPropertiesTo(Object target) {
    return map(this, target, false);
  }

  /**
   * Same as {@link #copyPropertiesTo(Object)} but allows the user to pass a flag to include null values.
   * Delegates to {@link #map(Object, Object, Boolean)}
   * @param target The object to receive this form's values.
   * @param includeNulls include null values?
   * @return true if the mapping was successful.
   */
  public Boolean copyPropertiesTo(Object target, Boolean includeNulls) {
    return map(this, target, includeNulls);
  }

  /**
   * Copy properties from an object to this form. Ignores null values by default.
   * Delegates to {@link #map(Object, Object, Boolean)}
   * @param source The object to copy properties from.
   * @return true if the mapping was successful.
   */
  public Boolean copyPropertiesFrom(Object source) {
    return map(source, this, false);
  }

  /**
   * Same as {@link #copyPropertiesFrom(Object)} but with the option to include null values.
   * Delegates to {@link #map(Object, Object, Boolean)}
   * @param source The object to copy properties from.
   * @param includeNulls include null values?
   * @return true if the mapping was successful.
   */
  public Boolean copyPropertiesFrom(Object source, Boolean includeNulls) {
    return map(source, this, includeNulls);
  }

  /**
   * Get an instance of a class from the {@link ApplicationContext}.
   * @param tClass The class of the instance to create.
   * @param <T> The return type.
   * @return An instance of T
   */
  public <T> T getInstanceOf(Class<T> tClass) {
    T instance = ApplicationContext.getBean(tClass);
    if(instance != null)
      map(this, instance, true);
    return instance;
  }

  /**
   * The method that copies properties from one object to another.
   * @param source The object to copy properties from.
   * @param target The object to copy properties to.
   * @param includeNulls Include null values?
   * @return true if the mapping was successful.
   */
  private Boolean map(Object source, Object target, Boolean includeNulls) {
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
          if(value == null && !includeNulls) continue;
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
