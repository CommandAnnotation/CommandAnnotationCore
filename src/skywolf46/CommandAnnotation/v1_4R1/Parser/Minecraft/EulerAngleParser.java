package skywolf46.CommandAnnotation.v1_4R1.Parser.Minecraft;

import org.bukkit.util.EulerAngle;
import skywolf46.CommandAnnotation.v1_4R1.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

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
