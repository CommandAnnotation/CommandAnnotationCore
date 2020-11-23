package skywolf46.commandannotation.data.methodprocessor;

import skywolf46.commandannotation.data.methodprocessor.exceptional.ExceptionStack;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

import java.util.HashMap;

public class GlobalData {
    private static ExceptionalHandler defaultHandler = new ExceptionalHandler();
    private static HashMap<String, ParameterMatchedInvoker> method = new HashMap<>();

    public static void handle(Throwable ex, ParameterStorage st, ExceptionStack stack) {
        ex = defaultHandler.handle(ex, st, stack);
        if (ex != null){
            ex.printStackTrace();
            System.out.println("Not handled");
        }

    }

    public static ExceptionalHandler getExceptionHandler() {
        return defaultHandler;
    }

    public static void registerMethod(String name, ParameterMatchedInvoker inv) {
        method.put(name, inv);
    }

    public static ParameterMatchedInvoker getMethod(String name) {
        return method.get(name);
    }
}
