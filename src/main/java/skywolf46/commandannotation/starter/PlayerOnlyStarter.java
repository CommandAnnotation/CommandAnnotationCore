package skywolf46.commandannotation.starter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skywolf46.commandannotation.abstraction.AbstractCommandStarter;
import skywolf46.commandannotation.annotations.handler.error.ExceptHandler;
import skywolf46.commandannotation.annotations.minecraft.PlayerOnly;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.data.methodprocessor.exceptional.ExceptionStack;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

public class PlayerOnlyStarter extends AbstractCommandStarter<PlayerOnly> {
    private String msg;

    public PlayerOnlyStarter(String msg) {
        this.msg = msg;
    }

    @Override
    public Class<PlayerOnly> getAnnotationClass() {
        return PlayerOnly.class;
    }

    @Override
    public boolean canProcessCommand(ParameterStorage storage) {
        if (storage.get(Player.class) != null) {
            return true;
        }
        storage.get(CommandSender.class)
                .sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return false;
    }

    @Override
    public AbstractCommandStarter<PlayerOnly> onCreate(PlayerOnly data, GlobalData gl) {
        return new PlayerOnlyStarter(data.value());
    }

    @Override
    public void process(ClassData.ClassDataBlueprint blueprint, MethodChain currentChain) {

    }
}
