package skywolf46.commandannotation.data.parser.impl.define;

import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;

public class StringDefine extends AbstractParseDefine<String> {
    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String parse(CommandArgument.CommandIterator iterator) throws Exception {
        return iterator.next();
    }

    @Override
    public AbstractParseDefine<String> createInstance(String obj) {
        return new StringDefine();
    }
}
