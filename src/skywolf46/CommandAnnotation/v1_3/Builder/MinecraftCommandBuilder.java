package skywolf46.CommandAnnotation.v1_3.Builder;

import skywolf46.CommandAnnotation.v1_3.API.AbstractCompletable;
import skywolf46.CommandAnnotation.v1_3.Data.CommandAction;
import skywolf46.CommandAnnotation.v1_3.Data.CommandArgument;
import skywolf46.CommandAnnotation.v1_3.API.MinecraftAbstractCommand;
import skywolf46.CommandAnnotation.v1_3.Data.CommandData;
import skywolf46.CommandAnnotation.v1_3.Minecraft.MinecraftCommandManager;
import skywolf46.CommandAnnotation.v1_3.Storage.CommandStorage;

import java.util.*;

public class MinecraftCommandBuilder {
    private List<String> commands = new ArrayList<>();
    private MinecraftCommandBuilder parent = null;
    private HashMap<String, MinecraftCommandBuilder> subcommand = new HashMap<>();
    private MinecraftCommandBuilder builder = this;
    private List<MinecraftAbstractCommand> currentAction = new ArrayList<>();
    private boolean autoComplete = true;
    private List<String> reqPermission = new ArrayList<>();
    private boolean fallbackOnSubcommandNotExist = true;
    private boolean useCommandEvent = false;

    public MinecraftCommandBuilder() {

    }

    private MinecraftCommandBuilder parent(MinecraftCommandBuilder mcb) {
        this.parent = mcb;
        return this;
    }


    public MinecraftCommandBuilder command(String... cmd) {
        if (builder.parent == null)
            commands.addAll(Arrays.asList(cmd));
        else {
            for (String n : cmd) {
                builder.parent.subcommand.put(n, builder);
            }
        }
        return this;
    }

    public MinecraftCommandBuilder fallBackOnSubcommandNotExist(boolean b) {
        this.fallbackOnSubcommandNotExist = b;
        return this;
    }

    public MinecraftCommandBuilder autoComplete(boolean b) {
        builder.autoComplete = b;
        return this;
    }

    public MinecraftCommandBuilder child(String cmd, MinecraftAbstractCommand action, boolean changeTargettoChild) {
        MinecraftCommandBuilder mcb = builder.subcommand.containsKey(cmd) ? builder.subcommand.get(cmd) : builder.subcommand.computeIfAbsent(cmd, key -> new MinecraftCommandBuilder());
        mcb.currentAction.add(action);
        mcb.parent = (mcb == this ? null : this.builder);
        if (changeTargettoChild)
            this.builder = mcb;
        return this;
    }

    public MinecraftCommandBuilder child(String cmd, MinecraftAbstractCommand action) {
        child(cmd, action, false);
        return this;
    }

    public MinecraftCommandBuilder parent(boolean top) {
        if (top)
            builder = this;
        else
            builder = builder.parent == null ? this : builder.parent;
        return this;
    }

    public MinecraftCommandBuilder parent() {
        parent(false);
        return this;
    }

    public MinecraftCommandBuilder useCommandEvent(boolean b) {
        this.useCommandEvent = b;
        return this;
    }

    public MinecraftCommandBuilder add(MinecraftAbstractCommand ac) {
        builder.currentAction.add(ac);
        return this;
    }

    private CommandAction buildCommandAction(String r, boolean isCommand) {
        return new AbstractCompletable() {
            private List<MinecraftAbstractCommand> mac = new ArrayList<>(currentAction);

            {
                // Lower number first
                mac.sort(new Comparator<MinecraftAbstractCommand>() {
                    @Override
                    public int compare(MinecraftAbstractCommand o1, MinecraftAbstractCommand o2) {
                        return Integer.compare(o1.getCommandPriority(), o2.getCommandPriority());
                    }
                });
            }

            @Override
            public boolean processAutoComplete(CommandArgument arg) {
                for (MinecraftAbstractCommand mc : mac)
                    if (!mc.processAutoComplete(arg))
                        return false;
                return true;
            }

            @Override
            public void editCompletion(String[] cmd, List<String> complete, String lastArgument) {
//                System.out.println("Auto completion requested at " + r);
                for (MinecraftAbstractCommand mc : mac) {
                    mc.editCompletion(cmd, complete, lastArgument);
                }
            }

            @Override
            public void active(Object[] o) {
                if (mac.size() <= 0)
                    return;
                CommandArgument argument = new CommandArgument();
                for (Object ob : o)
                    argument.add(ob);
                if (isCommand)
                    argument.setEventCancelled(true);
                for (int i = 0; i < mac.size(); i++) {
                    MinecraftAbstractCommand ac = mac.get(i);
                    if (argument.isCommandCancelled() && !ac.ignoreCommandCancel())
                        continue;
                    try {
                        ac.onCommand(argument);
                    } catch (Exception e) {
                        CommandData cd=  argument.get(CommandData.class);
                        String commandArgument =  cd.getCommand() + " " + cd.getCommandArgument(0,cd.length());
                        System.err.println("CommandAnnotation project failed to process command {" + commandArgument + "}(Command Index " + (i + 1) + " / Class " + ac.getClass().getName() + ") caused by " + e.getClass().getSimpleName());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String toString() {
                return mac.size() == 1 ? mac.get(0).toString() : super.toString();
            }
        };
    }

    private void complete(String root) {
        CommandStorage.registerCommand(root, buildCommandAction(root, root.startsWith("/")), fallbackOnSubcommandNotExist, useCommandEvent);
        if (autoComplete || useCommandEvent)
                MinecraftCommandManager.injectAutoComplete(root);
        for (Map.Entry<String, MinecraftCommandBuilder> mcb : subcommand.entrySet()) {
            mcb.getValue().complete(root + " " + mcb.getKey());
//            for(String alias : mcb.getValue().commands){
//                mcb.getValue().complete(root + " " + alias);
//                System.out.println("Alias of " + root + " " + mcb.getKey() + " {" + Arrays.toString(subcommand.keySet().toArray()) + "}");
//
//            }
        }
    }

    public void complete() {
        for (String current : commands) {
            complete(current);
        }
    }


}
