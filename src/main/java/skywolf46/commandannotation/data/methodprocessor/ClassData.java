package skywolf46.commandannotation.data.methodprocessor;

import skywolf46.commandannotation.annotations.common.*;
import skywolf46.commandannotation.annotations.handler.error.ExceptHandler;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.methodprocessor.exceptional.ExceptionStack;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassData {
    private ExceptionalHandler classHandler = new ExceptionalHandler();
    private HashMap<String, MethodChain> chain = new HashMap<>();

    public void handle(Throwable ex, ParameterStorage st, ExceptionStack stack) {
        if ((ex = classHandler.handle(ex, st)) == null) {
            return;
        }
        GlobalData.handle(ex, st, stack);
    }

    public static ClassData create(Class cl) {

        ClassData cd = new ClassData();
        List<Method> process = new ArrayList<>();
        for (Method mtd : cl.getMethods()) {
            process.add(mtd);
            if (Modifier.isStatic(mtd.getModifiers())) {
                // Static, process
                ExceptHandler handler = mtd.getAnnotation(ExceptHandler.class);
                boolean classApply = mtd.getAnnotation(ApplyClass.class) != null;
                boolean applyGlobal = mtd.getAnnotation(ApplyGlobal.class) != null;
                Mark mark = mtd.getAnnotation(Mark.class);
                ParameterMatchedInvoker invoker = null;
                if (mark != null) {
                    GlobalData.registerMethod(mark.value(), (invoker == null ? invoker = new ParameterMatchedInvoker(mtd) : invoker));
                }
                if (handler != null) {
                    if (classApply){
                        for (Class<? extends Throwable> ex : handler.value()){
                            cd.classHandler.registerExceptionHandler(ex, (invoker == null ? invoker = new ParameterMatchedInvoker(mtd) : invoker));
                        }
                    }
                    if (applyGlobal)
                        for (Class<? extends Throwable> ex : handler.value())
                            GlobalData.getExceptionHandler().registerExceptionHandler(ex, (invoker == null ? invoker = new ParameterMatchedInvoker(mtd) : invoker));
                }
            }
        }

        for (Method mtd : process) {
            MinecraftCommand cmd = mtd.getAnnotation(MinecraftCommand.class);
            if (cmd != null) {
                MethodChain chain = new MethodChain(cd, new ParameterMatchedInvoker(mtd));
                Redirect red = mtd.getAnnotation(Redirect.class);
                if (red != null) {

                }

                ErrorRedirect errRed = mtd.getAnnotation(ErrorRedirect.class);
                if (errRed != null) {
                    for (String x : errRed.value()) {
                        ParameterMatchedInvoker inv = GlobalData.getMethod(x);
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
                    cd.chain.put(xi, chain);
            }
        }
        return cd;
    }


    public MethodChain getChain(String name){
        return chain.get(name);
    }
}
