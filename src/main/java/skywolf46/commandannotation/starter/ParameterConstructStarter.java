package skywolf46.commandannotation.starter;

import skywolf46.commandannotation.abstraction.AbstractCommandStarter;
import skywolf46.commandannotation.annotations.common.ConstructParameters;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.util.ParameterStorage;

public class ParameterConstructStarter extends AbstractCommandStarter<ConstructParameters> {
    @Override
    public Class<ConstructParameters> getAnnotationClass() {
        return ConstructParameters.class;
    }

    @Override
    public boolean canProcessCommand(ParameterStorage storage) {
        return false;
    }

    @Override
    public AbstractCommandStarter<ConstructParameters> onCreate(ConstructParameters data, GlobalData gl) {
        return null;
    }
}
