package com.boltframework.utils;

public class Strings {
  public static String trimStart(String string, int chars) {
    return string.substring(chars);
  }

  public static String trimStart(String string) {
    return Strings.trimStart(string, 1);
  }

  public static String trimEnd(String string, int chars) {
    return string.substring(0, string.length() - chars);
  }

  public static String trimEnd(String string) {
    return Strings.trimEnd(string, 1);
  }

}
