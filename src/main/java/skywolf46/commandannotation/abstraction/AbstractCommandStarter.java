package skywolf46.commandannotation.abstraction;

import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.util.ParameterStorage;

import java.lang.annotation.Annotation;

public abstract class AbstractCommandStarter<X extends Annotation> {
    public abstract Class<X> getAnnotationClass();

    public abstract boolean canProcessCommand(ParameterStorage storage);

    public abstract AbstractCommandStarter onCreate(X data, GlobalData gl);

    public abstract void process(ClassData.ClassDataBlueprint blueprint, MethodChain currentChain);


}
