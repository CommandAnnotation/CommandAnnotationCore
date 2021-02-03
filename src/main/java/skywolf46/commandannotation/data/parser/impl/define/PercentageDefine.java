package skywolf46.commandannotation.data.parser.impl.define;

import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.exception.ParameterNotEnoughException;

public class PercentageDefine extends AbstractParseDefine<Double> {
    @Override
    public Class<Double> getType() {
        return Double.class;
    }

    @Override
    public Double parse(CommandArgument.CommandIterator iterator) throws Exception {
        if (iterator.left() < 1)
            throw new ParameterNotEnoughException();
        String next = iterator.next();
        if (next.endsWith("%"))
            next = next.substring(0, next.length() - 1);
        return Double.parseDouble(next);
    }

    @Override
    public AbstractParseDefine<Double> createInstance(Double obj) {
        return new PercentageDefine();
    }
}
