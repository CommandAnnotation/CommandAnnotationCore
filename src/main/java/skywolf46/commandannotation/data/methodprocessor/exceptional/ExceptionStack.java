package skywolf46.commandannotation.data.methodprocessor.exceptional;

import skywolf46.commandannotation.util.ParameterMatchedInvoker;

import java.util.ArrayList;
import java.util.List;

public class ExceptionStack {
    private List<ParameterMatchedInvoker> invokers = new ArrayList<>();

    public boolean handle(ParameterMatchedInvoker inv) {
        if (invokers.contains(inv)) {
            throw new IllegalStateException("Recursive exception occurred; Printing stacktrace");
        }
        invokers.add(inv);
        return true;
    }

    public void clear() {
        invokers.clear();
    }
}
