package skywolf46.commandannotation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.commandannotation.annotations.autocomplete.AutoCompleteProvider;
import skywolf46.commandannotation.annotations.common.ApplyClass;
import skywolf46.commandannotation.annotations.handler.error.ExceptHandler;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.minecraft.MinecraftCommandImpl;
import skywolf46.commandannotation.util.AutoCompleteUtil;
import skywolf46.commandannotation.util.JarUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandAnnotation extends JavaPlugin {
    public static final String VERSION = "2.0.1";
    private static CommandAnnotation inst;
    private static AtomicBoolean isEnabled = new AtomicBoolean(false);
    private static HashMap<String, Command> commands;
    private static HashMap<String, MinecraftCommandImpl> impl = new HashMap<>();
    private static HelpMap helps;

    @Override
    public void onEnable() {

    }

    public static void init(JavaPlugin pl) {
        if (!isEnabled.get()) {
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + VERSION + "§7 | §fInitialization...");
//            Bukkit.getPluginManager().registerEvents();
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + VERSION + "§7 | §fExtracting command map...");
            commands = AutoCompleteUtil.parseCommandMap();
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + VERSION + "§7 | §fExtracting help map...");
            helps = AutoCompleteUtil.parseHelpMap();
        }
        isEnabled.set(true);
        Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + VERSION + "§7 | §fInitializing " + pl.getName());
        GlobalData global = new GlobalData();
        try {
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + VERSION + "§7 | §fCreating blueprint from " + pl.getName() + "...");
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File file = (File) getFileMethod.invoke(pl);
            List<Class> cls = JarUtil.getAllClass(file);
            List<ClassData.ClassDataBlueprint> cbp = new ArrayList<>();
            for (Class c : cls) {
                ClassData.ClassDataBlueprint print = ClassData.create(global, c);
                cbp.add(print);
            }
            Bukkit.getConsoleSender().sendMessage("§aCommandAnnotation " + VERSION + "§7 | §fProcessing blueprint from " + pl.getName() + "...");
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
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
