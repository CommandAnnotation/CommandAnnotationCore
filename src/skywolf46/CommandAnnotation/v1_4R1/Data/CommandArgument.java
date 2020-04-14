package skywolf46.CommandAnnotation.v1_4R1.Data;

import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandArgument {
    private HashMap<Class, Object[]> objs = new HashMap<>();
    private HashMap<String, List<List>> namedVariable = new HashMap<>();
    private boolean isCommandCancelled = false;

    public <T> List<T> getNamed(String name,int index) {
        return (List<T>)namedVariable.get(name).get(index);
    }

    public <T> void addNamed(String name, List<T> t) {
        namedVariable.computeIfAbsent(name,a -> new ArrayList<>()).add(t);
    }

    public <T> T get(Class<T> cl) {
        return objs.get(cl) != null ? (T) objs.get(cl)[0] : null;
    }

    public <T> T[] getAll(Class<T> cl) {
        return (T[]) objs.getOrDefault(cl, new Object[0]);
    }

    public void add(Object o) {
        Class c = o.getClass();
        do {
            add(c, o);
            for (Class i : c.getInterfaces())
                addInterface(i, o);
        } while (!(c = c.getSuperclass()).equals(Object.class));
        add(Object.class, o);
    }

    public void addInterface(Class c, Object o) {
        add(c, o);
        for (Class ca : c.getInterfaces())
            addInterface(ca, o);
    }

    private void add(Class c, Object o) {
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

    public void removeFirst(Class c) {
        if (!objs.containsKey(c))
            return;
        Object[] o = objs.get(c);
        if (o.length == 1)
            objs.remove(c);
        else {
            objs.put(c, Arrays.copyOfRange(o, 1, o.length));
        }
    }

    public boolean isCommandCancelled() {
        return isCommandCancelled;
    }

    public void setCommandCancelled(boolean commandCancelled) {
        isCommandCancelled = commandCancelled;
    }

    public void setEventCancelled(boolean eventCancelled) {
        if (get(Cancellable.class) != null)
            get(Cancellable.class).setCancelled(eventCancelled);
    }

    public boolean isEventCancelled() {
        return get(Cancellable.class).isCancelled();
    }
}
