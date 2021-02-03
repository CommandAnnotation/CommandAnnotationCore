package skywolf46.commandannotation.data.parser.impl.define;

import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.exception.ParameterNotEnoughException;

public class IntegerDefine extends AbstractParseDefine<Integer> {
    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Integer parse(CommandArgument.CommandIterator iterator) throws Exception {
        if (iterator.left() < 1)
            throw new ParameterNotEnoughException();
        return Integer.parseInt(iterator.next());
    }

    @Override
    public AbstractParseDefine<Integer> createInstance(Integer obj) {
        return new IntegerDefine();
    }
}
