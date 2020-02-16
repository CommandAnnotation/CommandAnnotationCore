package skywolf46.CommandAnnotation.v1_3R2.Minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import skywolf46.CommandAnnotation.v1_3R2.Data.CommandActionWrapper;
import skywolf46.CommandAnnotation.v1_3R2.Data.CommandData;
import skywolf46.CommandAnnotation.v1_3R2.Storage.CommandStorage;

import java.util.List;

public class CommandListener implements Listener {
    @EventHandler
    public void listen(ServerCommandEvent e) {
        String[] split = ("/" + e.getCommand()).split(" ",2000);
        CommandActionWrapper ca = CommandStorage.getCommand(split);
        if(ca == null)
            return;
        if(!ca.useCommandEvent())
            return;
        CommandData cd = new CommandData(e.getCommand(),ca.getIndex());
        ca.getCommandAction().active(new Object[]{e,e.getCommand(),cd,e.getSender()});
    }

    @EventHandler
    public void listen(PlayerCommandPreprocessEvent e) {
        String[] split = e.getMessage().split(" ",2000);
        CommandActionWrapper ca = CommandStorage.getCommand(split);
        if(ca == null)
            return;
        if(!ca.useCommandEvent())
            return;
        CommandData cd = new CommandData(e.getMessage(),ca.getIndex());
        ca.getCommandAction().active(new Object[]{e,e.getMessage(),cd,e.getPlayer()});
    }

    @EventHandler
    public void listen(PlayerChatTabCompleteEvent e) {
        List<String> complete = MinecraftCommandManager.getChattingAutoComplete(e.getPlayer(),e.getChatMessage());
        if(complete.size() > 0){
            e.getTabCompletions().clear();
            e.getTabCompletions().addAll(complete);
        }
    }

    @EventHandler
    public void listen(PlayerChatEvent e) {
        String[] split = e.getMessage().split(" ",2000);
        CommandActionWrapper ca = CommandStorage.getCommand(split);
        if(ca == null)
            return;
        if(!ca.useCommandEvent())
            return;
        CommandData cd = new CommandData(e.getMessage(),ca.getIndex());
        ca.getCommandAction().active(new Object[]{e,e.getMessage(),cd,e.getPlayer()});
    }
}
