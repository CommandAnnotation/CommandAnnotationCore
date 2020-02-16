package skywolf46.CommandAnnotation.v1_3R2.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SectionCommandArgument extends CommandArgument {
    private HashMap<Class, Object[]> objs = new HashMap<>();
    private boolean useTemp = false;

    public <T> T get(Class<T> cl) {
        if (useTemp)
            return objs.get(cl) != null ? (T) objs.get(cl)[0] : super.get(cl);
        else
            return super.get(cl);
    }

    public <T> T[] getAll(Class<T> cl) {
        if (useTemp) {
            List<Object> list = new ArrayList<>();
            list.addAll(Arrays.asList(objs.getOrDefault(cl, new Object[0])));
            list.addAll(Arrays.asList(super.getAll(cl)));
            return (T[]) list.toArray(new Object[0]);
        } else
            return super.getAll(cl);
    }

    public void changeUsingTemp(boolean b) {
        this.useTemp = b;
    }

    public void addTemp(Object o) {
        Class c = o.getClass();
        do {
            addTemp(c, o);
            for (Class i : c.getInterfaces())
                addTemp(i, o);
        } while (!(c = c.getSuperclass()).equals(Object.class));
        addTemp(Object.class, o);
    }

    private void addTemp(Class c, Object o) {
        if (!objs.containsKey(c))
            objs.put(c, new Object[]{o});
        else {
            Object[] first = objs.get(c);
            Object[] second = new Object[first.length + 1];
            for (int i = 0; i < first.length; i++)
                second[i] = first[i];
            second[second.length - 1] = o;
            objs.put(c, second);
        }
    }

}
