package skywolf46.CommandAnnotation.v1_1.Util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AutoCompleteUtil {
    public static List<String> fetchStarting(List<String> str,String start){
        str = new ArrayList<>(str);
        Iterator<String> iter = str.iterator();
        while (iter.hasNext()){
            if(!iter.next().startsWith(start))
                iter.remove();
        }
        return str;
    }

    public static List<String> fetchOnlinePlayers(String start){
        List<String> players = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers())
            players.add(p.getName());
        return fetchStarting(players,start);
    }
}
