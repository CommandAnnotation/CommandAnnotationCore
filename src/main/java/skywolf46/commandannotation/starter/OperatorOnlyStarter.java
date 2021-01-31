package skywolf46.commandannotation.starter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import skywolf46.commandannotation.abstraction.AbstractCommandStarter;
import skywolf46.commandannotation.annotations.minecraft.OperatorOnly;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.util.ParameterStorage;

public class OperatorOnlyStarter extends AbstractCommandStarter<OperatorOnly> {
    private String msg;

    @Override
    public Class<OperatorOnly> getAnnotationClass() {
        return OperatorOnly.class;
    }

    @Override
    public boolean canProcessCommand(ParameterStorage storage) {
        CommandSender cs = storage.get(CommandSender.class);
        if (cs.isOp())
            return true;
        if (msg.isEmpty())
            return false;
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return false;
    }

    @Override
    public AbstractCommandStarter<OperatorOnly> onCreate(OperatorOnly data, GlobalData gl) {
        OperatorOnlyStarter starter = new OperatorOnlyStarter();
        starter.msg = data.value();
        return starter;
    }
}
