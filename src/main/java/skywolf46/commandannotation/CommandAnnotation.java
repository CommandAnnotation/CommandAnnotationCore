package skywolf46.commandannotation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.commandannotation.abstraction.AbstractCommandStarter;
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
                ClassData.ClassDataBlueprint print = ClassData.create(global, c);
                cbp.add(print);
            }
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + getVersion() + "§7 | §fProcessing blueprint from " + pl.getName() + "...");
//            System.out.println(cbp);
            for (ClassData.ClassDataBlueprint bp_ : cbp) {
                ClassData cd = bp_.process();
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

    public static boolean triggerSubCommand(Class<?> target, String command, ParameterStorage storage) throws Throwable {
        ClassData cd = proceed.computeIfAbsent(target, cl -> {
            ClassData.ClassDataBlueprint bp = ClassData.create(new GlobalData(), cl);
            return bp.process();
        });
        MethodChain sub = cd.getSubCommand(command);
        if (sub == null) {
            return false;
        }
        sub.invoke(storage);
        return true;
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
