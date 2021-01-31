package skywolf46.commandannotation.starter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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
    private String err;

    public NonPlayerOnlyStarter(String err) {
        this.err = err;
    }

    @Override
    public Class<NonPlayerOnly> getAnnotationClass() {
        return NonPlayerOnly.class;
    }


    @Override
    public boolean canProcessCommand(ParameterStorage storage) {
        if (storage.get(Player.class) == null) {
            return true;
        }
        storage.get(CommandSender.class)
                .sendMessage(ChatColor.translateAlternateColorCodes('&', err));
        return false;
    }

    @Override
    public AbstractCommandStarter<NonPlayerOnly> onCreate(NonPlayerOnly data, GlobalData gl) {
        //        sta.chainName = data.handler();

        return new NonPlayerOnlyStarter(data.value());
    }

    @Override
    public void process(ClassData.ClassDataBlueprint blueprint, MethodChain currentChain) {

    }
}
