package skywolf46.commandannotation.data.parser.impl.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.exception.ParameterNotEnoughException;

public class PlayerDefine extends AbstractParseDefine<Player> {
    @Override
    public Class<Player> getType() {
        return Player.class;
    }

    @Override
    public Player parse(CommandArgument.CommandIterator iterator) throws Exception {
        if (iterator.left() < 1)
            throw new ParameterNotEnoughException();
        return Bukkit.getPlayerExact(iterator.next());
    }

    @Override
    public AbstractParseDefine<Player> createInstance(Player obj) {
        return new PlayerDefine();
    }
}
