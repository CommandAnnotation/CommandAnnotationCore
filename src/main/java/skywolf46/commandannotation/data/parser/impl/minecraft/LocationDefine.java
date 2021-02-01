package skywolf46.commandannotation.data.parser.impl.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.exception.ParameterNotEnoughException;

public class LocationDefine extends AbstractParseDefine<Location> {
    @Override
    public Class<Location> getType() {
        return Location.class;
    }

    @Override
    public Location parse(CommandArgument.CommandIterator iterator) throws Exception {
        if (iterator.left() < 4)
            throw new ParameterNotEnoughException();
        return new Location(Bukkit.getWorld(iterator.next()), Double.parseDouble(iterator.next()), Double.parseDouble(iterator.next()), Double.parseDouble(iterator.next()));
    }

    @Override
    public AbstractParseDefine<Location> createInstance(Location obj) {
        return new LocationDefine();
    }
}
