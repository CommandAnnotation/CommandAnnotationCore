package skywolf46.commandannotation.util;

import java.lang.reflect.Method;

public class ReflectionUtil {

    public static <T> T invokeStatic(Method mtd) {
        return invokeStatic(mtd);
    }

    public static <T> T invoke(Method mtd, Object o) {
        try {
            return (T) mtd.invoke(o);
        } catch (Exception ex) {
            return null;
        }
    }
}
