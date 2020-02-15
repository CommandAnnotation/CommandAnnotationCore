package skywolf46.CommandAnnotation.v1_3.Parser;

import skywolf46.CommandAnnotation.v1_3.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3.Data.ParameterIterator;

public class DoublePaser extends ParameterParser<Double> {
    @Override
    public Double readParameter(ParameterIterator it) {
        return Double.parseDouble(it.next());
    }
}
