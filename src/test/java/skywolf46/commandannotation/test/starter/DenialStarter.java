package skywolf46.commandannotation.test.starter;

import skywolf46.commandannotation.abstraction.AbstractCommandStarter;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.test.starter.annotations.Denial;
import skywolf46.commandannotation.util.ParameterStorage;

public class DenialStarter extends AbstractCommandStarter<Denial> {
    private Denial denial;

    public DenialStarter(Denial denial) {
        this.denial = denial;
    }

    @Override
    public Class<Denial> getAnnotationClass() {
        return Denial.class;
    }

    @Override
    public boolean canProcessCommand(ParameterStorage storage) {
        return !denial.isDenial();
    }

    @Override
    public AbstractCommandStarter<Denial> onCreate(Denial data, GlobalData gl) {
        System.out.println("Denial!");
        return new DenialStarter(data);
    }

    @Override
    public void process(ClassData.ClassDataBlueprint blueprint, MethodChain currentChain) {

    }

    @Override
    public void onCommandDenied(ParameterStorage storage) {
        System.out.println("Command denied!");
    }
}
