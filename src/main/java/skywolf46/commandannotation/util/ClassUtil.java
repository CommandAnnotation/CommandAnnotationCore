package skywolf46.commandannotation.util;

import java.util.function.Consumer;

public class ClassUtil {
    private static final Class OBJECT = Object.class;

    public static void iterateParentClass(Class c, Consumer<Class> runner) {
//        System.out.println("Iterate");
        if (c.equals(OBJECT))
            return;
//        runner.accept(c);
        do {
//            System.out.println("Class: " + c);
            runner.accept(c);
            for (Class x : c.getInterfaces()) {
                runner.accept(x);
                iterateParentClass(x, runner);
            }
            System.out.println(c);
        } while ((c = c.getSuperclass()) != null && !c.equals(OBJECT));
    }
}
