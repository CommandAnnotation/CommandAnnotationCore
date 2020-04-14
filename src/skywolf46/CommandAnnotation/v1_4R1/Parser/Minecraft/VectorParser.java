package skywolf46.CommandAnnotation.v1_4R1.Parser.Minecraft;

import org.bukkit.util.Vector;
import skywolf46.CommandAnnotation.v1_4R1.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

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
