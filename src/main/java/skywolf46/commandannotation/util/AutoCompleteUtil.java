package skywolf46.commandannotation.util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AutoCompleteUtil {
    public static List<String> fetchStarting(List<String> str, String start) {
        if (start == null)
            start = "";
        str = new ArrayList<>(str);
        Iterator<String> iter = str.iterator();
        while (iter.hasNext()) {
            if (!iter.next().startsWith(start))
                iter.remove();
        }
        return str;
    }

    public static List<String> fetchOnlinePlayers(String start) {
        List<String> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers())
            players.add(p.getName());
        return fetchStarting(players, start);
    }

    @Deprecated
    public static HashMap<String, Command> parseCommandMap() {
        try {
            Class cl = SimpleCommandMap.class;
            Field fl = cl.getDeclaredField("knownCommands");
            fl.setAccessible(true);
            Class cx = Bukkit.getServer().getClass();
            Object target = cx.getMethod("getCommandMap").invoke(Bukkit.getServer());
            return (HashMap<String, Command>) fl.get(target);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Deprecated
    public static HelpMap parseHelpMap() {
        try {
            Class cx = Bukkit.getServer().getClass();
            Object target = cx.getMethod("getHelpMap").invoke(Bukkit.getServer());
            return (HelpMap) target;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
