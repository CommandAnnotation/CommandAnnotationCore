package skywolf46.commandannotation.data.methodprocessor;

import skywolf46.commandannotation.data.autocomplete.AutoCompleteSupplier;
import skywolf46.commandannotation.data.methodprocessor.exceptional.ExceptionStack;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

import java.util.HashMap;

public class GlobalData {
    private ExceptionalHandler defaultHandler = new ExceptionalHandler();
    private HashMap<String, ParameterMatchedInvoker> method = new HashMap<>();
    private HashMap<String, AutoCompleteSupplier> sup = new HashMap<>();
    private AutoCompleteSupplier defSupplier;


    public void handle(Throwable ex, ParameterStorage st, ExceptionStack stack) {
        ex = defaultHandler.handle(ex, st, stack);
        if (ex != null) {
            ex.printStackTrace();
            System.out.println("Not handled");
        }

    }

    public ExceptionalHandler getExceptionHandler() {
        return defaultHandler;
    }


    public ParameterMatchedInvoker getMethod(String name) {
        return method.get(name);
    }

    public AutoCompleteSupplier getAutoCompleteSupplier(String name) {
        return sup.get(name);
    }

    public void registerMethod(String name, ParameterMatchedInvoker inv) {
        method.put(name, inv);
    }

    public void registerAutoCompleteProvider(String key, AutoCompleteSupplier supplier) {
        sup.put(key, supplier);
    }

    public void setDefaultCompleter(AutoCompleteSupplier sup) {
        this.defSupplier = sup;
    }

    public AutoCompleteSupplier getAutoCompleteSupplier() {
        return defSupplier;
    }

}
