package skywolf46.commandannotation.data;

import skywolf46.commandannotation.util.ClassUtil;

import java.util.HashMap;

public class ExceptionRelay {
    private HashMap<Class<? extends Throwable>, ExceptionHandler> exceptHandler = new HashMap<>();

    public <T extends Throwable> ExceptionRelay handle(Class<T> cls, ExceptionHandler<T> consume) {
        exceptHandler.put(cls, consume);
        return this;
    }

    public static ExceptionRelay relay() {
        return new ExceptionRelay();
    }

    public <T extends Throwable> void handle(T ex) {
        ClassUtil.iterateParentClass(ex.getClass(), ev -> {
            if (exceptHandler.containsKey(ev))
                exceptHandler.get(ev).doIt(ex);
        });
    }

    @FunctionalInterface
    public interface ExceptionHandler<T extends Throwable> {
        void doIt(T tx);
    }
}
