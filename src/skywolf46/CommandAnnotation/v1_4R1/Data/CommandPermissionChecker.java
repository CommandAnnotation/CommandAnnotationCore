package skywolf46.CommandAnnotation.v1_4R1.Data;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Consumer;

public interface CommandPermissionChecker {
    static HashMap<String, CommandPermissionChecker> regedChecker = new HashMap<>();

    boolean checkPermission(CommandArgument cArg, CommandSender cs);

    public static void registerChecker(String name, CommandPermissionChecker cpc) {
        regedChecker.put(name, cpc);
    }

    public static CommandPermissionChecker getChecker(String name) {
        return regedChecker.get(name);
    }

    public static void registerPermission(String name, String perm, Consumer<CommandSender> sender) {
        regedChecker.put(name, new UserPermissionChecker(perm, sender));
    }

    public static void registerPlayerOnly(String name, Consumer<CommandSender> onNotPlayer) {
        regedChecker.put(name, new UserOnlyChecker(null, null, onNotPlayer));
    }

    public static void registerPlayerOnlyPermission(String name, String perm,Consumer<CommandSender> onNoPerm,Consumer<CommandSender> onNotPlayer) {
        regedChecker.put(name, new UserOnlyChecker(perm, onNoPerm, onNotPlayer));
    }

    public static class NoLimitPermissionChecker implements CommandPermissionChecker {
        public static final NoLimitPermissionChecker INST = new NoLimitPermissionChecker();

        @Override
        public boolean checkPermission(CommandArgument cArg, CommandSender cs) {
            return true;
        }
    }

    public static class UserPermissionChecker implements CommandPermissionChecker {
        private Consumer<CommandSender> cc;
        private String perm;

        public UserPermissionChecker(String perm, Consumer<CommandSender> cc) {
            this.cc = cc;
            this.perm = perm;
        }

        @Override
        public boolean checkPermission(CommandArgument cArg, CommandSender cs) {
            if (cs.hasPermission(perm))
                return true;
            cc.accept(cs);
            return false;
        }
    }

    public static class UserOnlyChecker implements CommandPermissionChecker {
        private Consumer<CommandSender> cc;
        private Consumer<CommandSender> op;
        private String perm;

        public UserOnlyChecker(String perm, Consumer<CommandSender> onNoPerm, Consumer<CommandSender> onNotPlayer) {
//            this.cc = cc;
            this.perm = perm;
            this.op = onNotPlayer;
            this.cc = onNoPerm;
        }

        @Override
        public boolean checkPermission(CommandArgument cArg, CommandSender cs) {
            if (!(cs instanceof Player)) {
                op.accept(cs);
                return false;
            }
            if (perm == null || cs.hasPermission(perm))
                return true;
            cc.accept(cs);
            return false;
        }
    }

}
