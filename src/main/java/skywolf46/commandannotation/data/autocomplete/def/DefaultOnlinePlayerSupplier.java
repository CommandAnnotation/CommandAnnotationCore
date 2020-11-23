package skywolf46.commandannotation.data.autocomplete.def;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import skywolf46.commandannotation.annotations.autocomplete.AutoCompleteProvider;

import java.util.List;

public class DefaultOnlinePlayerSupplier {

    @AutoCompleteProvider
    public static void def(List<String> def) {
        if (def.size() <= 0) {
            for(Player pl : Bukkit.getOnlinePlayers()){

            }
        }
    }
}
