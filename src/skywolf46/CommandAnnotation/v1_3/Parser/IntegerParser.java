package skywolf46.CommandAnnotation.v1_3.Parser;

import skywolf46.CommandAnnotation.v1_3.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3.Data.ParameterIterator;

public class IntegerParser extends ParameterParser<Integer> {
    @Override
    public Integer readParameter(ParameterIterator it) {
        return Integer.parseInt(it.next());
    }
}
