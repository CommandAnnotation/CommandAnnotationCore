package skywolf46.CommandAnnotation.v1_3.Minecraft;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import skywolf46.CommandAnnotation.v1_3.CommandAnnotation;
import skywolf46.CommandAnnotation.v1_3.Data.CommandAction;

import java.util.HashMap;

public class EventManager {
    private static HashMap<Class<Event>, EventCommandListener> ecl = new HashMap<>();

    public static void addEventManager(CommandAction ca, EventPriority pr,Class<Event>... ev) {
        for(Class<Event> ec : ev){
            ecl.computeIfAbsent(ec,key ->{
                EventCommandListener ecl = new EventCommandListener();
                Bukkit.getPluginManager().registerEvent(ec, new Listener() {
                },pr,ecl, CommandAnnotation.getInstance());
                return ecl;
            }).add(ca);
        }
    }
}
