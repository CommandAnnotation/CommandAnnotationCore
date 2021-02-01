package skywolf46.commandannotation.data.parser.impl.minecraft;

import org.bukkit.util.Vector;
import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.exception.ParameterNotEnoughException;

public class VectorDefine extends AbstractParseDefine<Vector> {
    @Override
    public Class<Vector> getType() {
        return Vector.class;
    }

    @Override
    public Vector parse(CommandArgument.CommandIterator iterator) throws Exception {
        if (iterator.left() < 4)
            throw new ParameterNotEnoughException();
        return new Vector(Double.parseDouble(iterator.next()), Double.parseDouble(iterator.next()), Double.parseDouble(iterator.next()));
    }

    @Override
    public AbstractParseDefine<Vector> createInstance(Vector obj) {
        return new VectorDefine();
    }
}
