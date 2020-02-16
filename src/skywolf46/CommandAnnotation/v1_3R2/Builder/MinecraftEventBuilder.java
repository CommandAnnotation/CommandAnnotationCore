package skywolf46.CommandAnnotation.v1_3R2.Builder;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import skywolf46.CommandAnnotation.v1_3R2.API.MinecraftEventCommand;
import skywolf46.CommandAnnotation.v1_3R2.Data.CommandAction;
import skywolf46.CommandAnnotation.v1_3R2.Data.SectionCommandArgument;
import skywolf46.CommandAnnotation.v1_3R2.Minecraft.EventCommandListener;
import skywolf46.CommandAnnotation.v1_3R2.Minecraft.EventManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MinecraftEventBuilder {
    private List<Class<Event>> events = new ArrayList<>();
    private List<List<MinecraftEventCommand>> eventPairs = new ArrayList<>();
    private EventPriority priority = EventPriority.NORMAL;
    public MinecraftEventBuilder(){
        next();
    }
    public MinecraftEventBuilder event(Class<?>... ev){
        for(Class b : ev){
            Class c = b;
            do {
                if(c.equals(Event.class))
                    events.add(b);

            }while ((c = c.getSuperclass()) != null && !c.equals(Object.class));
        }
        return this;
    }

    public MinecraftEventBuilder next(){
        eventPairs.add(new ArrayList<>());
        return this;
    }

    public MinecraftEventBuilder priority(EventPriority pr){
        this.priority = pr;
        return this;
    }

    public MinecraftEventBuilder add(MinecraftEventCommand ev){
        eventPairs.get(eventPairs.size()-1).add(ev);
        return this;
    }

    public void complete(){
        for(List<MinecraftEventCommand> list : eventPairs){
            CommandAction ca = new CommandAction() {
                private List<MinecraftEventCommand> mec = new ArrayList<>(list);
                {
                    mec.sort(new Comparator<MinecraftEventCommand>() {
                        @Override
                        public int compare(MinecraftEventCommand o1, MinecraftEventCommand o2) {
                            return Integer.compare(o1.getEventPriority(),o2.getEventPriority());
                        }
                    });
                }
                @Override
                public void active(Object[] o) {
                    SectionCommandArgument cArg = new SectionCommandArgument();
                    for(Object a : o)
                        cArg.add(a);
                    EventCommandListener.process(cArg);
                    boolean canceled = false;
                    for(MinecraftEventCommand me : mec){
                        if(canceled && me.ignoreCommandCancel())
                            return;
                        cArg.changeUsingTemp(me.supplyEventArgument());
                        canceled = me.onEvent(cArg);
                    }
                }
            };
            EventManager.addEventManager(ca,priority,events.toArray(new Class[0]));
        }
    }


}
