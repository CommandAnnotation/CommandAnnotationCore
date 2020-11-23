package skywolf46.commandannotation.data.methodprocessor;

import skywolf46.commandannotation.annotations.autocomplete.AutoCompleteProvider;
import skywolf46.commandannotation.annotations.common.*;
import skywolf46.commandannotation.annotations.handler.error.ExceptHandler;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.autocomplete.AutoCompleteSupplier;
import skywolf46.commandannotation.data.methodprocessor.exceptional.ExceptionStack;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClassData {
    private ExceptionalHandler classHandler = new ExceptionalHandler();
    private HashMap<String, MethodChain> chain = new HashMap<>();
    private GlobalData global;
    private AutoCompleteSupplier defaultCompleter;

    public ClassData(GlobalData gd) {
        this.global = gd;
    }

    public GlobalData getGlobal() {
        return global;
    }

    public void handle(Throwable ex, ParameterStorage st, ExceptionStack stack) {
        if ((ex = classHandler.handle(ex, st)) == null) {
            return;
        }
        getGlobal().handle(ex, st, stack);
    }

    public static ClassDataBlueprint create(GlobalData global, Class cl) {
        ClassData cd = new ClassData(global);
        List<Method> process = new ArrayList<>();
        ClassDataBlueprint bp = new ClassDataBlueprint(cl, process, cd);
        for (Method mtd : cl.getMethods()) {
            process.add(mtd);
            if (Modifier.isStatic(mtd.getModifiers())) {
                // Static, process
                ExceptHandler handler = mtd.getAnnotation(ExceptHandler.class);
                boolean classApply = mtd.getAnnotation(ApplyClass.class) != null;
                boolean applyGlobal = mtd.getAnnotation(ApplyGlobal.class) != null;
                Mark mark = mtd.getAnnotation(Mark.class);
                AutoCompleteProvider prov = mtd.getAnnotation(AutoCompleteProvider.class);
                ParameterMatchedInvoker invoker = null;
                if (mark != null) {
                    global.registerMethod(mark.value(), (invoker == null ? invoker = new ParameterMatchedInvoker(mtd) : invoker));
                }
                if (handler != null) {
                    if (classApply) {
                        for (Class<? extends Throwable> ex : handler.value()) {
                            cd.classHandler.registerExceptionHandler(ex, (invoker == null ? invoker = new ParameterMatchedInvoker(mtd) : invoker));
                        }
                    }
                    if (applyGlobal)
                        for (Class<? extends Throwable> ex : handler.value())
                            global.getExceptionHandler().registerExceptionHandler(ex, (invoker == null ? invoker = new ParameterMatchedInvoker(mtd) : invoker));
                }

                if (prov != null) {

                }
            }
        }
        return bp;
    }


    public MethodChain getChain(String name) {
        return chain.get(name);
    }

    public static class ClassDataBlueprint {
        private List<Method> mtds = new ArrayList<>();
        private ClassData orig;
        private Class cl;
        private AtomicBoolean proceed = new AtomicBoolean(false);

        private ClassDataBlueprint(Class cl, List<Method> mtd, ClassData cd) {
            this.cl = cl;
            this.mtds = mtd;
            this.orig = cd;
        }


        public ClassData process() {
            if (proceed.get()) {
                throw new IllegalStateException("ClassData blueprint already used");
            }
            proceed.set(true);
            for (Method mtd : mtds) {
                AutoCompleteProvider cplt = mtd.getAnnotation(AutoCompleteProvider.class);
                if (cplt != null) {
                    MethodChain chain = new MethodChain(orig, new ParameterMatchedInvoker(mtd));
                    Redirect red = mtd.getAnnotation(Redirect.class);
                    if (red != null) {
                        System.err.println("AutoCompleteSupplier not supports redirection. Ignoring in method " + mtd.getName() + " at " + cl.getName());
                    }
                    ErrorRedirect errRed = mtd.getAnnotation(ErrorRedirect.class);
                    if (errRed != null) {
                        for (String x : errRed.value()) {
                            ParameterMatchedInvoker inv = orig.getGlobal().getMethod(x);
                            if (inv != null) {
                                ExceptHandler handle = inv.getMethod().getAnnotation(ExceptHandler.class);
                                if (handle == null)
                                    System.err.println("Method " + mtd.getName() + " from " + cl.getClass().getSimpleName() + " try to use unexisting error handler" + x + ", ignoring.");
                                else
                                    for (Class<? extends Throwable> ex : handle.value())
                                        chain.getHandler().registerExceptionHandler(ex, inv);
                            } else {
                                System.err.println("Method " + mtd.getName() + " from " + cl.getClass().getSimpleName() + " try to use unknown error handler " + x + ", ignoring.");
                            }
                        }
                    }
                    AutoCompleteSupplier sup = AutoCompleteSupplier.from(chain);
                    if (sup == null) {
                        System.err.println("Method " + mtd.getName() + " in " + cl.getName() + " declared as AutoCompleteProvider, but return value is not List or array, and parameter not contains List. AutoCompleteProvider register denied.");
                    } else {
                        if (!cplt.value().isEmpty())
                            orig.getGlobal().registerAutoCompleteProvider(cplt.value(), sup);
                        if (mtd.getAnnotation(ApplyClass.class) != null) {
                            orig.setDefaultCompleter(sup);
                        }

                        if (mtd.getAnnotation(ApplyGlobal.class) != null) {
                            orig.getGlobal().setDefaultCompleter(sup);
                        }
                    }
                }
            }
            for (Method mtd : mtds) {
                MinecraftCommand cmd = mtd.getAnnotation(MinecraftCommand.class);
                if (cmd != null) {
                    MethodChain chain = new MethodChain(orig, new ParameterMatchedInvoker(mtd));
                    Redirect red = mtd.getAnnotation(Redirect.class);
                    if (red != null) {

                    }

                    ErrorRedirect errRed = mtd.getAnnotation(ErrorRedirect.class);
                    if (errRed != null) {
                        for (String x : errRed.value()) {
                            ParameterMatchedInvoker inv = orig.getGlobal().getMethod(x);
                            if (inv != null) {
                                ExceptHandler handle = inv.getMethod().getAnnotation(ExceptHandler.class);
                                if (handle == null)
                                    System.err.println("Method " + mtd.getName() + " from " + cl.getClass().getSimpleName() + " try to use unexisting error handler" + x + ", ignoring.");
                                else
                                    for (Class<? extends Throwable> ex : handle.value())
                                        chain.getHandler().registerExceptionHandler(ex, inv);
                            } else {
                                System.err.println("Method " + mtd.getName() + " from " + cl.getClass().getSimpleName() + " try to use unknown error handler " + x + ", ignoring.");
                            }
                        }
                    }
                    for (String xi : cmd.command())
                        orig.chain.put(xi, chain);
                }
            }
            return orig;
        }
    }

    private void setDefaultCompleter(AutoCompleteSupplier sup) {
        this.defaultCompleter = sup;
    }
}
