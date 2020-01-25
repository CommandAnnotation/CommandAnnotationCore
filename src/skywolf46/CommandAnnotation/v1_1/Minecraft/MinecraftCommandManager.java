package skywolf46.CommandAnnotation.v1_1.Minecraft;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import skywolf46.CommandAnnotation.v1_1.Data.CommandArgument;
import skywolf46.CommandAnnotation.v1_1.Data.CommandData;
import skywolf46.CommandAnnotation.v1_1.Storage.CommandStorage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MinecraftCommandManager {
    // Try injection
//    static {
//        injectPluginManager();
//    }


    private static HashMap<String, Command> map;
    private static HashMap<String, AutoCompleteManager> acm = new HashMap<>();
    private static CompleteManager chattingComplete = new CompleteManager();
    private static AtomicBoolean init = new AtomicBoolean(false);
    public static void injectPluginManager() {
        if(init.get())
            return;
        init.set(true);
        try {
            SimplePluginManager manager = (SimplePluginManager) Bukkit.getPluginManager();
            Field f = manager.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            CommandMap cMap = (CommandMap) f.get(manager);
            f = cMap.getClass().getDeclaredField("knownCommands");
            f.setAccessible(true);
            map = (HashMap<String, Command>) f.get(cMap);
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("§aProject CommandAnnotation §7| §cFailed to inject plugin manager : " + ex.getClass().getSimpleName());
        }
    }

    public static boolean injectAutoComplete(String command) {
        if (command.startsWith("/")) {
            String[] split = command.substring(1).split(" ",2000);
            if (acm.containsKey(split[0]))
                acm.get(split[0]).getCompleteManager().addComplete(Arrays.copyOfRange(split, 1, split.length));
            else {
                AutoCompleteManager com = new AutoCompleteManager(split[0], map.get(split[0]));
                com.getCompleteManager().addComplete(Arrays.copyOfRange(split, 1, split.length));
                acm.put(split[0], com);
                map.put(split[0], com);
            }
        } else {
            String[] split = command.split(" ",2000);
            chattingComplete.addComplete(split);
        }
        return true;
    }

    public static List<String> getChattingAutoComplete(Player p,String current){
        String[] split = current.split(" ",2000);
        CommandArgument arg = new CommandArgument();
        CommandData cda = new CommandData(current,split.length-1);
        arg.add(cda);
        arg.add(p);
        List<String> complete = CommandStorage.fetchCompletable(Arrays.copyOf(split,split.length-1),new ArrayList<>(chattingComplete.getComplete(split)),arg);
        CommandStorage.getCompleteArgument(Arrays.copyOf(split,split.length-1),complete,split[split.length-1]);
        return complete;
    }


}
