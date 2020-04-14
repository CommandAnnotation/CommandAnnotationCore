package skywolf46.CommandAnnotation.v1_4R1.Parser;

import skywolf46.CommandAnnotation.v1_4R1.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

public class FloatParser extends ParameterParser<Float> {
    @Override
    public Float readParameter(ParameterIterator it) {
        return Float.parseFloat(it.next());
    }
}
