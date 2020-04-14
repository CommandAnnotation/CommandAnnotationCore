package skywolf46.CommandAnnotation.v1_4R1.Parser.Minecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import skywolf46.CommandAnnotation.v1_4R1.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

public class PlayerParser extends ParameterParser<Player> {
    @Override
    public Player readParameter(ParameterIterator it) {
        Player p = Bukkit.getPlayerExact(it.next());
        if (p == null)
            throw new NullPointerException();
        return p;
    }
}