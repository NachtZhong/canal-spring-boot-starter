package io.github.tequilacn.starter.reflect;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypes {
  private final static Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPERS = new HashMap<>();
  private final static Map<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVES = new HashMap<>();

  static{

    add(boolean.class, Boolean.class);
    add(byte.class, Byte.class);
    add(char.class, Character.class);
    add(double.class, Double.class);
    add(float.class, Float.class);
    add(int.class, Integer.class);
    add(long.class, Long.class);
    add(short.class, Short.class);
    add(void.class, Void.class);
  }

  private static void add(final Class<?> primitiveType, final Class<?> wrapperType) {
    PRIMITIVE_TO_WRAPPERS.put(primitiveType, wrapperType);
    WRAPPERS_TO_PRIMITIVES.put(wrapperType, primitiveType);
  }

  public static Class<?> getWrapper(final Class<?> primitiveType) {
    return PRIMITIVE_TO_WRAPPERS.get(primitiveType);
  }

  public static Class<?> getPrimitive(final Class<?> wrapperType) {
    return WRAPPERS_TO_PRIMITIVES.get(wrapperType);
  }
}