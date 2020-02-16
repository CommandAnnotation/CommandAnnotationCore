package skywolf46.CommandAnnotation.v1_3R2.API;

import skywolf46.CommandAnnotation.v1_3R2.Data.ParameterIterator;

public abstract class ParameterParser<T> {
    public abstract T readParameter(ParameterIterator it);

}
