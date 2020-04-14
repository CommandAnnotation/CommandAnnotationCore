package skywolf46.CommandAnnotation.v1_4R1.Parser;

import skywolf46.CommandAnnotation.v1_4R1.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

public class DoubleParser extends ParameterParser<Double> {
    @Override
    public Double readParameter(ParameterIterator it) {
        return Double.parseDouble(it.next());
    }
}
