package skywolf46.CommandAnnotation.v1_3.Parser.Minecraft;

import org.bukkit.util.Vector;
import skywolf46.CommandAnnotation.v1_3.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3.Data.ParameterIterator;

public class VectorParser extends ParameterParser<Vector> {
    @Override
    public Vector readParameter(ParameterIterator it) {
        return new Vector(
                it.next(Double.class),
                it.next(Double.class),
                it.next(Double.class)
        );
    }
}
