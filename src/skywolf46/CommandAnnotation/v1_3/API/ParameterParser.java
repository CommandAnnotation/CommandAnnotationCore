package skywolf46.CommandAnnotation.v1_3.API;

import skywolf46.CommandAnnotation.v1_3.Data.ParameterIterator;

public abstract class ParameterParser<T> {
    public abstract T readParameter(ParameterIterator it);

}
