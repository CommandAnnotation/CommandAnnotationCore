package skywolf46.commandannotation.data.parser.impl.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.exception.ParameterNotEnoughException;

public class OfflinePlayerDefine extends AbstractParseDefine<OfflinePlayer> {
    @Override
    public Class<OfflinePlayer> getType() {
        return OfflinePlayer.class;
    }

    @Override
    public OfflinePlayer parse(CommandArgument.CommandIterator iterator) throws Exception {
        if (iterator.left() < 1)
            throw new ParameterNotEnoughException();
        OfflinePlayer ofp = Bukkit.getPlayer(iterator.next());
        if (ofp == null)
            ofp = Bukkit.getOfflinePlayer(iterator.peekPrevious());
        return ofp;
    }

    @Override
    public AbstractParseDefine<OfflinePlayer> createInstance(OfflinePlayer obj) {
        return new OfflinePlayerDefine();
    }
}
