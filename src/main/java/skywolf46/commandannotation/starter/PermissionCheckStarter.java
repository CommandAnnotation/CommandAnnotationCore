package skywolf46.commandannotation.starter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import skywolf46.commandannotation.abstraction.AbstractCommandStarter;
import skywolf46.commandannotation.annotations.minecraft.PermissionHandler;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.util.ParameterStorage;

public class PermissionCheckStarter extends AbstractCommandStarter<PermissionHandler> {
    private String permission;
    private String msg;
    private boolean skipIfOp = false;

    @Override
    public Class<PermissionHandler> getAnnotationClass() {
        return PermissionHandler.class;
    }

    @Override
    public boolean canProcessCommand(ParameterStorage storage) {
        CommandSender cs = storage.get(CommandSender.class);
        if (skipIfOp) {
            if (cs.isOp())
                return true;
        }
        if (cs.hasPermission(permission)) {
            return true;
        }
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return false;
    }

    @Override
    public AbstractCommandStarter<PermissionHandler> onCreate(PermissionHandler data, GlobalData gl) {
        PermissionCheckStarter check = new PermissionCheckStarter();
        check.permission = data.value();
        check.msg = data.message();
        check.skipIfOp = data.skipCheckIfOperator();
        return check;
    }
}
