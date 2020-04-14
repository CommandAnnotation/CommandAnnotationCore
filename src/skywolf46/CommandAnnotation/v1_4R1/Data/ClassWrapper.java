package skywolf46.CommandAnnotation.v1_4R1.Data;

import skywolf46.CommandAnnotation.v1_4R1.Util.ClassChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassWrapper {
    private Class c;

    public ClassWrapper(Class c) {
//        System.out.println("ClassWrapper created - " + c);
        this.c = ClassChecker.getCompatibleClass(c);
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ClassWrapper))
            return false;
        Class c = this.c;
        Class tg = ((ClassWrapper) obj).c;
//        System.out.println("Instance class = " + tg.getSimpleName());
//        Class c = obj.getClass();
        do{
//            System.out.println("Target name = " + c.getSimpleName());
            if(c.equals(tg))
                return true;
        }while ((c = ClassChecker.getSuperClass(c)) != null);
        List<Class> ca = new ArrayList<>(Arrays.asList(c.getInterfaces()));
        for(Class a : tg.getInterfaces())
            if(ca.contains(a))
                return true;
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return c.toString();
    }
}
