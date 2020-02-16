package skywolf46.CommandAnnotation.v1_3R2.Parser.Minecraft;

import org.bukkit.Bukkit;
import org.bukkit.World;
import skywolf46.CommandAnnotation.v1_3R2.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3R2.Data.ParameterIterator;

public class WorldParser extends ParameterParser<World> {

    @Override
    public World readParameter(ParameterIterator it) {
        World w = Bukkit.getWorld(it.next());
        if(w == null)
            throw new NullPointerException();
        return w;
    }
}
