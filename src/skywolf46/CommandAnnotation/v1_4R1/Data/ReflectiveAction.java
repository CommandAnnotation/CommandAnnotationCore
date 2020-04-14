package skywolf46.CommandAnnotation.v1_4R1.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectiveAction extends CommandAction {
    private Method toInvoke;
    private CommandAttribute attribute;
    private List<Class> methodParameter = new ArrayList<>();
    private List<ClassWrapper> methodWrapper = new ArrayList<>();

    public ReflectiveAction(Method toInvoke, CommandAttribute attribute) {
        this.attribute = attribute;
        this.toInvoke = toInvoke;
        methodParameter.addAll(Arrays.asList(toInvoke.getParameterTypes()));
        for (Class c : methodParameter)
            methodWrapper.add(new ClassWrapper(c));
    }

    @Override
    public void active(Object... o) {
        List<Object> list = new ArrayList<>(Arrays.asList(o));
        if (!attribute.isParameterObjectMatch(list)) {
//            System.out.println("Parameter not match : skipping.");
            return;
        }
        CommandArgument arg = new CommandArgument();
        for (Object a : o)
            arg.add(a);
        Object[] param = attribute.sortParameter(arg, methodParameter, list);
//        System.out.println(Arrays.toString(param));
        try {
            toInvoke.invoke(null, param);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
