package skywolf46.CommandAnnotation.v1_4R1.Parser;

import skywolf46.CommandAnnotation.v1_4R1.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

public class StringParser extends ParameterParser<String> {
    @Override
    public String readParameter(ParameterIterator it) {
        return it.next();
    }
}
