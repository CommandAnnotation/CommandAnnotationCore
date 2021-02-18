package skywolf46.commandannotation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.commandannotation.abstraction.AbstractCommandStarter;
import skywolf46.commandannotation.annotations.common.DeprecatedDescription;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.minecraft.MinecraftCommandImpl;
import skywolf46.commandannotation.starter.NonPlayerOnlyStarter;
import skywolf46.commandannotation.starter.OperatorOnlyStarter;
import skywolf46.commandannotation.starter.PermissionCheckStarter;
import skywolf46.commandannotation.starter.PlayerOnlyStarter;
import skywolf46.commandannotation.util.AutoCompleteUtil;
import skywolf46.commandannotation.util.JarUtil;
import skywolf46.commandannotation.util.ParameterStorage;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandAnnotation extends JavaPlugin {

    private static CommandAnnotation inst;
    private static AtomicBoolean isEnabled = new AtomicBoolean(false);
    private static HashMap<String, Command> commands;
    private static HashMap<String, MinecraftCommandImpl> impl = new HashMap<>();
    private static HelpMap helps;
    private static HashMap<Class<? extends Annotation>, AbstractCommandStarter> scanTarget = new HashMap<>();
    private static HashMap<Class<?>, ClassData> proceed = new HashMap<>();

    @Override
    public void onEnable() {
        inst = this;
    }

    public static String getVersion() {
        return inst.getDescription().getVersion();
    }

    public static void init(JavaPlugin pl) {
        if (!isEnabled.get()) {
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + getVersion() + "§7 | §fInitialization...");
//            Bukkit.getPluginManager().registerEvents();
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + getVersion() + "§7 | §fExtracting command map...");
            commands = AutoCompleteUtil.parseCommandMap();
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + getVersion() + "§7 | §fExtracting help map...");
            helps = AutoCompleteUtil.parseHelpMap();
            registerScanTarget(new NonPlayerOnlyStarter(""));
            registerScanTarget(new PlayerOnlyStarter(""));
            registerScanTarget(new PermissionCheckStarter());
            registerScanTarget(new OperatorOnlyStarter());
        }
        isEnabled.set(true);
        Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + getVersion() + "§7 | §fInitializing " + pl.getName());
        GlobalData global = new GlobalData();
        try {
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + getVersion() + "§7 | §fCreating blueprint from " + pl.getName() + "...");
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File file = (File) getFileMethod.invoke(pl);
            List<Class<?>> cls = JarUtil.getAllClass(file);
            List<ClassData.ClassDataBlueprint> cbp = new ArrayList<>();
            for (Class<?> c : cls) {
                ClassData.ClassDataBlueprint print = ClassData.create(global, c, null);
                cbp.add(print);
            }
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + getVersion() + "§7 | §fProcessing blueprint from " + pl.getName() + "...");
//            System.out.println(cbp);
            for (ClassData.ClassDataBlueprint bp_ : cbp) {
                ClassData cd = bp_.process(null);
                for (String n : cd.getCommands()) {
                    String[] xl = n.split(" ");
                    impl.computeIfAbsent(xl[0], a -> {
                        MinecraftCommandImpl impl = new MinecraftCommandImpl(n);
                        commands.put(a, impl);
                        return impl;
                    }).insert(xl, cd.getChain(n));
                }
                proceed.put(cd.getOriginalClass(), cd);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Deprecated
    @DeprecatedDescription({
            "CommandAnnotation designed for static method registration to remove confusion on command registering.",
            "Object command registration feature will not removed, but will be deprecated forever."
    })
    public static void registerCommandObject(Object obj) {
        ClassData cd = ClassData.create(new GlobalData(), obj.getClass(), null).process(obj);
        for (String n : cd.getCommands()) {
            String[] xl = n.split(" ");
            impl.computeIfAbsent(xl[0], a -> {
                MinecraftCommandImpl impl = new MinecraftCommandImpl(n);
                commands.put(a, impl);
                return impl;
            }).insert(xl, cd.getChain(n));
        }
//        proceed.put(cd.getOriginalClass(), cd);
    }

    public static boolean triggerSubCommand(Class<?> target, String command, ParameterStorage storage) throws Throwable {
        ClassData cd = proceed.computeIfAbsent(target, cl -> {
            ClassData.ClassDataBlueprint bp = ClassData.create(new GlobalData(), cl, null);
            return bp.process(null);
        });
        MethodChain sub = cd.getSubCommand(command);
        if (sub == null) {
            return false;
        }
        sub.invoke(storage);
        return true;
    }

    public static void triggerAutoComplete(Class<?> target, String command, ParameterStorage storage, List<String> cplt) throws Throwable {
        cplt.clear();
        ClassData cd = proceed.computeIfAbsent(target, cl -> {
            ClassData.ClassDataBlueprint bp = ClassData.create(new GlobalData(), cl, null);
            return bp.process(null);
        });
        if (cd.getSubCommand(command) == null) {
            cplt.addAll(cd.getSubCommands());
            AutoCompleteUtil.fetchStarting(cplt, command);
            return;
        }
        MethodChain mc = cd.getSubCommand(command);
        if (mc.getCompleteSupplier() != null) {
            mc.getCompleteSupplier().editCompletion(storage, cplt);
            return;
        }
        if (cd.getSupplier() != null) {
            cd.getSupplier().editCompletion(storage, cplt);
            return;
        }

        if (cd.getGlobal().getAutoCompleteSupplier() != null) {
            cd.getGlobal().getAutoCompleteSupplier().editCompletion(storage, cplt);
        }

    }


    public static boolean triggerSubCommand(Class<?> target, CommandArgument nextArgs, ParameterStorage storage) throws Throwable {
        nextArgs = nextArgs.clone();
        ClassData cd = proceed.computeIfAbsent(target, cl -> {
            ClassData.ClassDataBlueprint bp = ClassData.create(new GlobalData(), cl, null);
            return bp.process(null);
        });
        StringBuilder args = new StringBuilder();
        while (nextArgs.length() > 0) {
            if (args.length() != 0)
                args.append(" ");
            args.append(nextArgs.get(0));
            nextArgs.nextPointer();
            MethodChain sub = cd.getSubCommand(args.toString());
            if (sub != null) {
                storage.set(nextArgs);
                sub.invoke(storage);
                return true;
            }
        }
        return false;
    }


    public static void registerScanTarget(AbstractCommandStarter starter) {
        scanTarget.put(starter.getAnnotationClass(), starter);
    }

    public static List<Class<? extends Annotation>> getScanTargets() {
        return new ArrayList<>(scanTarget.keySet());
    }

    public static AbstractCommandStarter getStarter(Class<? extends Annotation> anot) {
        return scanTarget.get(anot);
    }

}
