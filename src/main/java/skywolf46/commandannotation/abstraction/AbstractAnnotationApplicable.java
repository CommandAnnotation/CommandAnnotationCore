package skywolf46.commandannotation.abstraction;

import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.util.ParameterStorage;

import java.lang.annotation.Annotation;
import java.util.HashMap;

public abstract class AbstractAnnotationApplicable {
    private static HashMap<Class<? extends Annotation>, AbstractCommandStarter<?>> starters = new HashMap<>();

    public void addStarter(AbstractCommandStarter<?> starter) {
        starters.put(starter.getAnnotationClass(), starter);
    }

    public boolean canProcess(ParameterStorage storage) {
        for (AbstractCommandStarter<?> st : starters.values())
            if (!st.canProcessCommand(storage)) {
                st.onCommandDenied(storage);
                return false;
            }
        return true;
    }

    public void processBlueprint(ClassData.ClassDataBlueprint bp) {
        for (AbstractCommandStarter<?> st : starters.values())
            st.process(bp, (MethodChain) this);
    }
}
