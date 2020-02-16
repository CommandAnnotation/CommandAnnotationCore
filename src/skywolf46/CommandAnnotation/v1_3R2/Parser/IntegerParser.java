package skywolf46.CommandAnnotation.v1_3R2.Parser;

import skywolf46.CommandAnnotation.v1_3R2.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3R2.Data.ParameterIterator;

public class IntegerParser extends ParameterParser<Integer> {
    @Override
    public Integer readParameter(ParameterIterator it) {
        return Integer.parseInt(it.next());
    }
}
