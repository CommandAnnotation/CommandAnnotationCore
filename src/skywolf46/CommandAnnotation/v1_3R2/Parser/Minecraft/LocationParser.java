package skywolf46.CommandAnnotation.v1_3R2.Parser.Minecraft;

import org.bukkit.Location;
import org.bukkit.World;
import skywolf46.CommandAnnotation.v1_3R2.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3R2.Data.ParameterIterator;

public class LocationParser extends ParameterParser<Location> {
    @Override
    public Location readParameter(ParameterIterator it) {
        return new Location(it.next(World.class), it.next(Double.class), it.next(Double.class), it.next(Double.class));
    }
}
