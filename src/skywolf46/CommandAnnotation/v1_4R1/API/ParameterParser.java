package skywolf46.CommandAnnotation.v1_4R1.API;

import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

public abstract class ParameterParser<T> {
    public abstract T readParameter(ParameterIterator it);

}
