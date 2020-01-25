package skywolf46.CommandAnnotation.v1_1.Util;

import java.util.HashMap;

public class ClassChecker {
    private static final Class<Object> OBJ_CLASS = Object.class;
    private static final HashMap<Class,Class> conveter = new HashMap<>();
    static {
        conveter.put(Integer.TYPE,Integer.class);
        conveter.put(Float.TYPE,Float.class);
        conveter.put(Double.TYPE,Double.class);
        conveter.put(Boolean.TYPE,Boolean.class);
        conveter.put(Short.TYPE,Short.class);
        conveter.put(Byte.TYPE,Byte.class);
        conveter.put(Long.TYPE,Long.class);
    }
//    public static boolean isClass(Object obj,Class c){
//
//    }

    public static Class<?> getSuperClass(Class<?> cl){
        if(cl.equals(OBJ_CLASS))
            return null;
        cl = cl.getSuperclass();
        if(cl.equals(OBJ_CLASS))
            return null;
        return cl;
    }

    public static Class getCompatibleClass(Class ca){
        if(conveter.containsKey(ca))
            return conveter.get(ca);
        return ca;
    }
}
