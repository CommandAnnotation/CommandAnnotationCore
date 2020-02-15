package skywolf46.CommandAnnotation.v1_3.Util;

import java.lang.reflect.Method;

public class ReflectUtil {
    public static Method getMethod(Class c, String mtd) {
        for (Method m : c.getMethods())
            if (m.getName().equals(mtd))
                return m;
        return null;
    }
}
