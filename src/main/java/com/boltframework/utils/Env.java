package com.boltframework.utils;

import com.sun.istack.internal.NotNull;

import javax.annotation.Nullable;

public class Env {

  @Nullable
  public static String getString(String key) {
    return get(key);
  }

  public static String getString(String key, String defaultValue) {
    return getString(key) == null ? defaultValue : getString(key);
  }

  @Nullable
  public static Integer getInt(String key) {
    String value = get(key);
    return value == null ? null : Integer.parseInt(value);
  }

  public static Integer getInt(String key, int defaultValue) {
    if(getInt(key) == null) return defaultValue;
    else return getInt(key);
  }

  @NotNull
  public static Boolean getBoolean(String key) {
    return Boolean.valueOf(get(key));
  }

  private static String get(String key) {
    String value = System.getProperty(key);
    if(value == null) value = System.getenv().get(key);
    return  value;
  }

  public static Boolean isProd() {
    String mode = getString("app.mode");
    return mode != null && mode.toLowerCase().matches("prod.*");
  }

  public static Boolean isDev() {
    String mode = getString("app.mode");
    return mode == null || mode.toLowerCase().matches("dev.*");
  }

}
