package skywolf46.commandannotation.data.methodprocessor;

import skywolf46.commandannotation.data.methodprocessor.exceptional.ExceptionStack;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

import java.util.HashMap;

public class ExceptionalHandler {
    private HashMap<Class<? extends Throwable>, ParameterMatchedInvoker> handlers = new HashMap<>();


    public void registerExceptionHandler(Class<? extends Throwable> tr, ParameterMatchedInvoker inv) {
        handlers.put(tr, inv);
    }

    public Throwable handle(Throwable ex, ParameterStorage storage) {
        return handle(ex, storage, new ExceptionStack());
    }

    public Throwable handle(Throwable ex, ParameterStorage storage, ExceptionStack stack) {
        storage.set(ex);
        ParameterMatchedInvoker voke = null;
        Class<?> cl = ex.getClass();
        while (voke == null) {
            voke = handlers.get(cl);
            if (cl.equals(Throwable.class))
                break;
            cl = cl.getSuperclass();
        }
        if (voke == null)
            return ex;

        stack.handle(voke);
        try {
            voke.invoke(storage);
        } catch (Throwable exi) {
            return handle(exi, storage, stack);
        }
        return null;
    }

}
