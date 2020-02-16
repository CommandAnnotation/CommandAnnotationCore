package skywolf46.CommandAnnotation.v1_3R2.Parser.Minecraft;

import org.bukkit.util.EulerAngle;
import skywolf46.CommandAnnotation.v1_3R2.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3R2.Data.ParameterIterator;

public class EulerAngleParser extends ParameterParser<EulerAngle> {
    @Override
    public EulerAngle readParameter(ParameterIterator it) {
        return new EulerAngle(
                it.next(Double.class),
                it.next(Double.class),
                it.next(Double.class)
        );
    }
}
