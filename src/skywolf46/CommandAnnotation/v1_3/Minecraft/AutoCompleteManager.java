package skywolf46.CommandAnnotation.v1_3.Minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import skywolf46.CommandAnnotation.v1_3.Data.CommandActionWrapper;
import skywolf46.CommandAnnotation.v1_3.Data.CommandArgument;
import skywolf46.CommandAnnotation.v1_3.Data.CommandData;
import skywolf46.CommandAnnotation.v1_3.Storage.CommandStorage;

import java.util.List;

public class AutoCompleteManager extends Command {
    private CompleteManager completeManager = new CompleteManager();
    private Command orig;
    private boolean callEvent;

    public AutoCompleteManager(String name, boolean callEvent) {
        super(name);
        this.callEvent = callEvent;
    }

    public AutoCompleteManager(String name) {
        super(name);
    }

    public AutoCompleteManager(String name, Command original) {
        super(name);
        this.orig = original;
    }

    public CompleteManager getCompleteManager() {
        return completeManager;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        // Dummy executor
        if (callEvent) {
            if (orig != null)
                orig.execute(commandSender, s, strings);
        } else {
            String[] n = new String[strings.length + 1];
            n[0] = "/" + s;
            System.arraycopy(strings, 0, n, 1, strings.length);
            CommandActionWrapper ca = CommandStorage.getCommand(n);
            if (ca == null)
                return false;
            StringBuilder cmd = new StringBuilder("/" + s);
            for (String na : strings)
                cmd.append(" ").append(na);

            CommandData cd = new CommandData(cmd.toString(), ca.getIndex());
            ca.getCommandAction().active(new Object[]{cmd.toString(), cd, commandSender});
        }
        return true;
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        alias = "/" + alias;
        CommandArgument cArg = new CommandArgument();
        cArg.add(sender);
        StringBuilder sb = new StringBuilder(alias);
        for (String n : args)
            sb.append(" ").append(n);
        CommandData cda = new CommandData(sb.toString(), 0);
        cArg.add(cda);
        List<String> cmd = completeManager.getComplete(args);
        String[] totalArg = (alias + " " + cda.getRealCommandArgument(0, args.length - 1)).split(" ");
        CommandStorage.fetchCompletable(totalArg, cmd, cArg);
        CommandStorage.getCompleteArgument(totalArg, cmd, args[args.length - 1]);
        return cmd;
    }
}
