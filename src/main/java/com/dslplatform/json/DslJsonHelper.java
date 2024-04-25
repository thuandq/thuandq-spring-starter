package com.dslplatform.json;

public class DslJsonHelper {
  public static <T> void reset(JsonReader<T> reader) {
    reader.reset();
  }
}
