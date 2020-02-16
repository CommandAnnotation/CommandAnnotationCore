package skywolf46.CommandAnnotation.v1_3R2.Parser;

import skywolf46.CommandAnnotation.v1_3R2.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3R2.Data.ParameterIterator;

public class FloatParser extends ParameterParser<Float> {
    @Override
    public Float readParameter(ParameterIterator it) {
        return Float.parseFloat(it.next());
    }
}
