package skywolf46.CommandAnnotation.v1_3.Parser;

import skywolf46.CommandAnnotation.v1_3.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3.Data.ParameterIterator;

public class FloatParser extends ParameterParser<Float> {
    @Override
    public Float readParameter(ParameterIterator it) {
        return Float.parseFloat(it.next());
    }
}
