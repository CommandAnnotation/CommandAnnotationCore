package skywolf46.CommandAnnotation.v1_4R1.Data;

import java.lang.reflect.Method;

public class MinecraftReflectionCommand extends ReflectiveAction{
    public MinecraftReflectionCommand(Method toInvoke, CommandAttribute attribute) {
        super(toInvoke, attribute);
    }


}