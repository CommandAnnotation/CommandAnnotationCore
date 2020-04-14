package skywolf46.CommandAnnotation.v1_4R1.Parser;

import skywolf46.CommandAnnotation.v1_4R1.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

public class IntegerParser extends ParameterParser<Integer> {
    @Override
    public Integer readParameter(ParameterIterator it) {
        return Integer.parseInt(it.next());
    }
}
