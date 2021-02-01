package skywolf46.commandannotation.util;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveConverter {
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<>();

    static {
        primitiveWrapperMap.put(boolean.class, Boolean.class);
        primitiveWrapperMap.put(byte.class, Byte.class);
        primitiveWrapperMap.put(char.class, Character.class);
        primitiveWrapperMap.put(double.class, Double.class);
        primitiveWrapperMap.put(float.class, Float.class);
        primitiveWrapperMap.put(int.class, Integer.class);
        primitiveWrapperMap.put(long.class, Long.class);
        primitiveWrapperMap.put(short.class, Short.class);
    }

    public static Class<?> boxPrimitive(Class<?> cls) {
        if (cls.isPrimitive())
            return primitiveWrapperMap.get(cls);
        return cls;
    }

}
