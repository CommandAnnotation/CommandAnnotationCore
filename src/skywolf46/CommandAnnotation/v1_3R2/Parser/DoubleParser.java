package skywolf46.CommandAnnotation.v1_3R2.Parser;

import skywolf46.CommandAnnotation.v1_3R2.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3R2.Data.ParameterIterator;

public class DoubleParser extends ParameterParser<Double> {
    @Override
    public Double readParameter(ParameterIterator it) {
        return Double.parseDouble(it.next());
    }
}
