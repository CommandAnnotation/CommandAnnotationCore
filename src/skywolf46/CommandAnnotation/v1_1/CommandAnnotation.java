package skywolf46.CommandAnnotation.v1_1;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.CommandAnnotation.v1_1.Annotations.MinecraftCommand;
import skywolf46.CommandAnnotation.v1_1.Data.*;
import skywolf46.CommandAnnotation.v1_1.Minecraft.CommandListener;
import skywolf46.CommandAnnotation.v1_1.Minecraft.MinecraftCommandManager;
import skywolf46.CommandAnnotation.v1_1.Storage.CommandStorage;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandAnnotation extends JavaPlugin {
    private static CommandAnnotation inst;
    private static AtomicBoolean isEnabled = new AtomicBoolean(false);
    @Override
    public void onEnable() {
        forceInit(this);
//        MinecraftAbstractCommand.builder()
//                .command("!test","/test9213","/!test!","/test test 123","!test test 13","!test test 14")
//                .autoComplete(true)
//                .child("wa", new MinecraftAbstractCommand() {
//                    @Override
//                    public boolean onCommand(CommandArgument args) {
//                        args.get(Player.class).sendMessage("Test");
//                        return false;
//                    }
//
//                    @Override
//                    public int getCommandPriority() {
//                        return 0;
//                    }
//                })
//                .child("test", new MinecraftAbstractCommand() {
//                    @Override
//                    public boolean onCommand(CommandArgument args) {
//                        args.get(Player.class).sendMessage("Test complete");
//                        return false;
//                    }
//
//                    @Override
//                    public int getCommandPriority() {
//                        return 0;
//                    }
//
//                    @Override
//                    public boolean processAutoComplete(CommandArgument arg) {
//                        return arg.get(CommandSender.class).hasPermission("test.perm");
//                    }
//
//                    @Override
//                    public void editCompletion(List<String> complete, String lastArgument) {
//                        System.out.println("Test " + lastArgument);
//                    }
//                })
//                .add(new MinecraftAbstractCommand() {
//                    @Override
//                    public boolean onCommand(CommandArgument args) {
//                        args.get(Player.class).sendMessage("Main command");
//                        return false;
//                    }
//
//                    @Override
//                    public int getCommandPriority() {
//                        return 0;
//                    }
//
//                    @Override
//                    public void editCompletion(List<String> complete, String lastArgument) {
//                        System.out.println("Test2 " + lastArgument);
//                    }
//                }).complete();
//        scanPackage(getClass());
//

    }

    public static void forceInit(JavaPlugin pl){
        if(isEnabled.get())
            return;
        isEnabled.set(true);
        Bukkit.getConsoleSender().sendMessage("§aProject CommandAnnotation §7| §fStarting...");
//        inst = this;
        Bukkit.getPluginManager().registerEvents(new CommandListener(),pl);
        MinecraftCommandManager.injectPluginManager();
        Bukkit.getConsoleSender().sendMessage("§aProject CommandAnnotation §7| §fSystem all green");
    }

    public static CommandAnnotation getInstance() {
        return inst;
    }

    private static Vector<Class> list(ClassLoader CL)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        Class CL_class = ClassLoader.class;
        java.lang.reflect.Field ClassLoader_classes_field = CL_class
                .getDeclaredField("classes");
        ClassLoader_classes_field.setAccessible(true);
        return (Vector) ClassLoader_classes_field.get(CL);
    }

    public static void scanPackage(Class... classes){
        try {
            for (Class c : classes) {
//                if (!c.getName().startsWith(packageName))
//                    continue;
                for (Method mtd : c.getMethods()) {
                    if (Modifier.isStatic(mtd.getModifiers())) {
                        MinecraftCommand mc = mtd.getAnnotation(MinecraftCommand.class);
                        if (mc != null) {
                             ReflectiveAction rf = new ReflectiveAction(mtd,
                                    new CommandAttribute(mc.command())
                                            .requireParameter(mc.requireParameter())
                                            .autoComplete(mc.autoComplete())
                                            .fallBack(mc.fallbackOnSubCommandNotExist())
                            );
                            for (String n : mc.command())
                                CommandStorage.registerCommand(n, rf, mc.fallbackOnSubCommandNotExist(),mc.useCommandEvent());
                        }
                    } else
                        continue;
                }
            }
        }catch (Exception ex){

        }
    }




    @MinecraftCommand(command = {"/?Test"})
    public static void test(Player p, CommandSender cs){
        if(p == null)
            cs.sendMessage("Player only command!");
        else
            p.sendMessage("Why so serious?");

    }
}
