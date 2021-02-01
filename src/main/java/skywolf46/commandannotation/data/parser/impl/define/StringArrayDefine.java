package skywolf46.commandannotation.data.parser.impl.define;

import lombok.AllArgsConstructor;
import lombok.Getter;
import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;

@AllArgsConstructor
public class StringArrayDefine extends AbstractParseDefine<String[]> {
    @Getter
    private int amount;
    @Getter
    private boolean softLimit;


    public static StringArrayDefine of(boolean softLimit, int amount) {
        return new StringArrayDefine(amount, softLimit);
    }

    @Override
    public Class<String[]> getType() {
        return String[].class;
    }

    @Override
    public String[] parse(CommandArgument.CommandIterator iterator) throws Exception {
        if (iterator.left() < getAmount()) {
            if (!softLimit) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        String[] arrs = new String[Math.min(iterator.left(), getAmount())];
        for (int i = 0; i < arrs.length; i++) {
            arrs[i] = iterator.next();
        }
        return arrs;
    }

    @Override
    public AbstractParseDefine<String[]> createInstance(String[] obj) {
        return StringArrayDefine.of(false, obj.length);
    }

}
