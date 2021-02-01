package skywolf46.commandannotation.data.parser.impl.define;

import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;

public class IntegerArrayDefine extends AbstractParseDefine<Integer[]> {
    private int length;

    public IntegerArrayDefine(int length) {
        this.length = length;
    }

    @Override
    public Class<Integer[]> getType() {
        return Integer[].class;
    }

    @Override
    public Integer[] parse(CommandArgument.CommandIterator iterator) throws Exception {
        Integer[] ax = new Integer[length];
        for (int i = 0; i < length; i++) {
            ax[i] = Integer.parseInt(iterator.next());
        }
        return ax;
    }

    @Override
    public AbstractParseDefine<Integer[]> createInstance(Integer[] obj) {
        return new IntegerArrayDefine(obj.length);
    }
}
