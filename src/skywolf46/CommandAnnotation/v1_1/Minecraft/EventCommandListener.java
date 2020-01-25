package skywolf46.CommandAnnotation.v1_1.Minecraft;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.plugin.EventExecutor;
import skywolf46.CommandAnnotation.v1_1.Data.CommandAction;
import skywolf46.CommandAnnotation.v1_1.Data.SectionCommandArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventCommandListener implements EventExecutor {
    private List<CommandAction> actions = new ArrayList<>();
    private static HashMap<Class, Consumer<SectionCommandArgument>> consumer = new HashMap<>();

    static {
        consumer.put(PlayerEvent.class, (arg) -> {
            PlayerEvent e = arg.get(PlayerEvent.class);
            arg.addTemp(e.getPlayer());
        });

        consumer.put(BlockEvent.class, (arg) -> {
            BlockEvent e = arg.get(BlockEvent.class);
            arg.addTemp(e.getBlock());
        });

        consumer.put(EntityEvent.class, arg -> {
            EntityEvent e = arg.get(EntityEvent.class);
            arg.addTemp(e.getEntity());
            arg.addTemp(e.getEntityType());
        });
        consumer.put(VehicleEvent.class, (arg) -> {
            VehicleEvent e = arg.get(VehicleEvent.class);
            arg.addTemp(e.getVehicle());
        });

        consumer.put(BlockBreakEvent.class,(arg) ->{
            BlockBreakEvent e = arg.get(BlockBreakEvent.class);
            arg.addTemp(e.getPlayer());
        });

        consumer.put(BlockPlaceEvent.class,(arg) ->{
            BlockPlaceEvent e = arg.get(BlockPlaceEvent.class);
            arg.addTemp(e.getPlayer());
        });

        consumer.put(PlayerInteractAtEntityEvent.class,(arg) ->{
            PlayerInteractAtEntityEvent e = arg.get(PlayerInteractAtEntityEvent.class);
            arg.addTemp(e.getRightClicked());
            arg.addTemp(e.getPlayer());
            arg.addTemp(e.getClickedPosition());
        });
        consumer.put(PlayerInteractEntityEvent.class,(arg) ->{
            PlayerInteractEntityEvent e = arg.get(PlayerInteractEntityEvent.class);
            arg.addTemp(e.getRightClicked());
            arg.addTemp(e.getPlayer());
        });

        consumer.put(PlayerInteractEvent.class,arg -> {
            PlayerInteractEvent e = arg.get(PlayerInteractEvent.class);
            arg.addTemp(e.getAction());
            arg.addTemp(e.getClickedBlock());
            arg.addTemp(e.getBlockFace());
            arg.addTemp(e.getHand());
            arg.addTemp(e.getMaterial());
            arg.addTemp(e.getItem());
        });


    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        for(CommandAction c : actions){
            c.active(new Object[]{event});
        }
    }

    public static void process(SectionCommandArgument ca){
        for(Map.Entry<Class,Consumer<SectionCommandArgument>> c : consumer.entrySet()){
            Object o = ca.get(c.getKey());
            if(o != null)
                c.getValue().accept(ca);
        }
    }

    public void add(CommandAction ca) {
        actions.add(ca);
    }
}
