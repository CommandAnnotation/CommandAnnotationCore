package skywolf46.commandannotation.starter;

import org.bukkit.entity.Player;
import skywolf46.commandannotation.abstraction.AbstractCommandStarter;
import skywolf46.commandannotation.annotations.minecraft.NonPlayerOnly;
import skywolf46.commandannotation.annotations.minecraft.PlayerOnly;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.data.methodprocessor.exceptional.ExceptionStack;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

public class NonPlayerOnlyStarter extends AbstractCommandStarter<NonPlayerOnly> {
    private ParameterMatchedInvoker chain = null;
    private String chainName;
    private ClassData cd;


    @Override
    public Class<NonPlayerOnly> getAnnotationClass() {
        return NonPlayerOnly.class;
    }


    @Override
    public boolean canProcessCommand(ParameterStorage storage) {
        if (storage.get(Player.class) != null) {
            return true;
        }
        if (chain != null) {
            try {
                chain.invoke(storage);
            } catch (Throwable throwable) {
                cd.handle(throwable, storage, new ExceptionStack());
            }
        }
        return false;
    }

    @Override
    public AbstractCommandStarter onCreate(NonPlayerOnly data, GlobalData gl) {
        PlayerOnlyStarter sta = new PlayerOnlyStarter();
//        sta.chainName = data.handler();
        return sta;
    }

    @Override
    public void process(ClassData.ClassDataBlueprint blueprint, MethodChain currentChain) {
        chain = blueprint.getClassData().getGlobal().getMethod(chainName);
        cd = blueprint.getClassData();
    }
}
