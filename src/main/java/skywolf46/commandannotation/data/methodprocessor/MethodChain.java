package skywolf46.commandannotation.data.methodprocessor;

import skywolf46.commandannotation.abstraction.AbstractAnnotationApplicable;
import skywolf46.commandannotation.data.autocomplete.AutoCompleteSupplier;
import skywolf46.commandannotation.data.methodprocessor.exceptional.ExceptionStack;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

public class MethodChain extends AbstractAnnotationApplicable {
    private ClassData parentData;
    private ExceptionalHandler handler = new ExceptionalHandler();
    private MethodChain redirection = null;
    private AutoCompleteSupplier defSupplier;
    private ParameterMatchedInvoker invoker;

    public MethodChain(ClassData parent, ParameterMatchedInvoker invoker) {
        this.parentData = parent;
        this.invoker = invoker;
    }

    public <T> T invoke(ParameterStorage storage) throws Throwable {
        try {
            if (!super.canProcess(storage))
                return null;
            return invoker.invoke(storage);
        } catch (Throwable ex) {
//            System.out.println("Handle?");
            handleException(ex, storage);
        }
        return null;
    }

    public void handleException(Throwable ex, ParameterStorage storage) throws Throwable {
        ExceptionStack stack = new ExceptionStack();
        ex = handler.handle(ex, storage, stack);
        if (ex != null) {
            parentData.handle(ex, storage, stack);
        }
    }

    public ParameterMatchedInvoker getInvoker() {
        return invoker;
    }

    public ExceptionalHandler getHandler() {
        return handler;
    }

    public void setHandler(ExceptionalHandler handler) {
        this.handler = handler;
    }

    public MethodChain getRedirection() {
        return redirection;
    }

    public void setRedirection(MethodChain redirection) {
        this.redirection = redirection;
    }

    public AutoCompleteSupplier getCompleteSupplier() {
        return defSupplier == null ? parentData.getSupplier() : defSupplier;
    }

    public void setDefSupplier(AutoCompleteSupplier defSupplier) {
        this.defSupplier = defSupplier;
    }


}
