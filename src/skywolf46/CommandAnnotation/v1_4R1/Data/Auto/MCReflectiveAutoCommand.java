package skywolf46.CommandAnnotation.v1_4R1.Data.Auto;


import skywolf46.CommandAnnotation.v1_4R1.API.AbstractCompletable;
import skywolf46.CommandAnnotation.v1_4R1.Annotations.$_;
import skywolf46.CommandAnnotation.v1_4R1.Data.CommandArgument;
import skywolf46.CommandAnnotation.v1_4R1.Data.CommandData;
import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;
import skywolf46.CommandAnnotation.v1_4R1.Data.ReflectionData;
import skywolf46.CommandAnnotation.v1_4R1.Minecraft.MinecraftCommandManager;
import skywolf46.CommandAnnotation.v1_4R1.Storage.CommandStorage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MCReflectiveAutoCommand extends AbstractCompletable {
    private static HashMap<String, List<MCReflectiveAutoCommand>> autoList = new HashMap<>();
    private Method targetMethod;
    private String cmd;
    private ReflectionData[] classes;
    private boolean autoComplete;
    private boolean fallBack;
    private boolean useCommandEvent;
    private List<AutoMatchingDataType> types = new ArrayList<>();

    public MCReflectiveAutoCommand(Method mtd, $_ cmd, String parent) {
        this.targetMethod = mtd;
        this.cmd = cmd.value();
        useCommandEvent = cmd.useCommandEvent();
        this.fallBack = cmd.fallBackIfNotExists();
        if (parent != null)
            this.cmd = parent + " " + this.cmd;
        this.autoComplete = cmd.autoComplete();
        classes = new ReflectionData[mtd.getParameters().length];
        Parameter[] param = mtd.getParameters();

        for (int i = 0; i < classes.length; i++) {
            classes[i] = new ReflectionData(param[i]);
        }
    }

    @Override
    public void active(Object[] o) {
        CommandArgument argument = new CommandArgument();
        for (Object ob : o)
            argument.add(ob);
        if (useCommandEvent)
            argument.setEventCancelled(true);

        try {
            this.invoke(argument);
        } catch (Exception e) {
            CommandData cd = argument.get(CommandData.class);
            String commandArgument = cd.getCommand() + " " + cd.getCommandArgument(0, cd.length());
            System.err.println("CommandAnnotation project failed to process command {" + commandArgument + "}(Reflection Command \"" + commandArgument + "\" / Class " + getClass().getName() + ") caused by " + e.getClass().getSimpleName());
            e.printStackTrace();
        }
//        this.invoke();
    }

    public boolean invoke(CommandArgument args) {
        ParameterIterator iter = args.get(CommandData.class).iterator();
        try {
            for (AutoMatchingDataType atdt : types)
                args.addNamed(atdt.getType(), atdt.parse(iter));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        args.add(args);
        HashMap<String, Integer> val = new HashMap<>();
        Object[] obj = new Object[classes.length];
        for (int i = 0; i < classes.length; i++)
            obj[i] = classes[i].matchObject(args,val);
        try {
            targetMethod.invoke(null, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static List<MCReflectiveAutoCommand> parse(Class c) {
        String parent = null;
        $_ mcc = ($_) c.getAnnotation($_.class);
        if (mcc != null)
            parent = mcc.value();
        List<MCReflectiveAutoCommand> ref = new ArrayList<>();
        for (Method m : c.getMethods()) {
            $_ anon = m.getAnnotation($_.class);
            if (anon == null)
                continue;
//            System.out.println("$_ value!");
            ref.add(new MCReflectiveAutoCommand(m, anon, parent));
        }
        return ref;
    }

    public String getCommand() {
        return cmd;
    }

    @Override
    public void editCompletion(String[] commands, List<String> complete, String lastArgument) {

    }

    public void register() {
        System.out.println("Target Command: " + this.cmd);
        CommandStorage.registerCommand(this.cmd, this, this.fallBack, this.useCommandEvent);
        if (autoComplete || useCommandEvent)
            MinecraftCommandManager.injectAutoComplete(this.cmd);
    }
}
