package skywolf46.commandannotation.data.autocomplete;

import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.exception.autocomplete.AutoCompleteTypeMismatchException;
import skywolf46.commandannotation.util.ParameterStorage;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AutoCompleteSupplier {
    private final MethodChain chain;


    public AutoCompleteSupplier(MethodChain chain) {
        this.chain = chain;
    }

    public MethodChain getChain() {
        return chain;
    }

    public abstract void editCompletion(ParameterStorage storage, List<String> str) throws Throwable;

    public static boolean check(Method mtd) {
        if (List.class.isAssignableFrom(mtd.getReturnType()) || String[].class.isAssignableFrom(mtd.getReturnType())) {
            return true;
        }
        for (Parameter pl : mtd.getParameters()) {
            if (List.class.isAssignableFrom(pl.getType())) {
                return true;
            }
        }
        return false;
    }

    public static AutoCompleteSupplier from(MethodChain mtd) {

        if (String[].class.isAssignableFrom(mtd.getInvoker().getMethod().getReturnType())) {
            return new StringArrayCompleteSupplier(mtd);
        }
        if (List.class.isAssignableFrom(mtd.getInvoker().getMethod().getReturnType())) {
            return new StringListCompleteSupplier(mtd);
        }
        for (Parameter pl : mtd.getInvoker().getMethod().getParameters()) {
            if (List.class.isAssignableFrom(pl.getType())) {
                return new ParameterCompleteSupplier(mtd);
            }
        }
        return null;
    }

    static class StringArrayCompleteSupplier extends AutoCompleteSupplier {

        public StringArrayCompleteSupplier(MethodChain chain) {
            super(chain);
        }

        @Override
        public void editCompletion(ParameterStorage st, List<String> str) throws Throwable {
            st.set(str);
            String[] arr = getChain().invoke(st);
            if (arr == null)
                return;
            str.addAll(Arrays.asList(arr));
        }
    }

    static class StringListCompleteSupplier extends AutoCompleteSupplier {

        public StringListCompleteSupplier(MethodChain chain) {
            super(chain);
        }

        @Override
        public void editCompletion(ParameterStorage storage, List<String> str) throws Throwable {
            storage.set(str);
            List<String> xt = getChain().invoke(storage);
            if (xt == null)
                return;
            try {
                if (xt.size() == 0)
                    str.clear();
                else {
                    if (!(xt.get(0) instanceof String))
                        throw new Exception();
                    str.clear();
                    str.addAll(xt);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                getChain().handleException(new AutoCompleteTypeMismatchException(xt.get(0)), storage);
            }
        }
    }

    static class ParameterCompleteSupplier extends AutoCompleteSupplier {

        public ParameterCompleteSupplier(MethodChain chain) {
            super(chain);
        }

        @Override
        public void editCompletion(ParameterStorage storage, List<String> str) throws Throwable {
            storage.set(str);
            getChain().invoke(storage);
        }
    }
}
