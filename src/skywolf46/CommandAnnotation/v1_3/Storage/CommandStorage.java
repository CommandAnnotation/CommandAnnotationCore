package skywolf46.CommandAnnotation.v1_3.Storage;

import skywolf46.CommandAnnotation.v1_3.API.AbstractCompletable;
import skywolf46.CommandAnnotation.v1_3.Data.CommandAction;
import skywolf46.CommandAnnotation.v1_3.Data.CommandActionWrapper;
import skywolf46.CommandAnnotation.v1_3.Data.CommandArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CommandStorage {
    private HashMap<String, CommandStorage> storage = new HashMap<>();
    private static CommandStorage mainStorage = new CommandStorage("Heroes of the storm", -1);
    private CommandActionWrapper action = null;
    private String currentArgment;
    private int currentIndex;
    private boolean fallBackOnSubcommandNotExist = true;

    private CommandStorage(String command, int currentIndex) {
        this.currentArgment = command;
        this.currentIndex = currentIndex;
    }

    private static CommandStorage getMainStorage() {
        return mainStorage;
    }

    public static void registerCommand(String cmd, CommandAction action, boolean fallBackOnSubcommandNotExist,boolean useCommandEvent) {
        String[] split = cmd.split(" ");
        getMainStorage().registerCommand(cmd, split, action, fallBackOnSubcommandNotExist,useCommandEvent);
    }

    public static List<String> fetchCompletable(String[] index,List<String> cmd, CommandArgument arg) {
        Iterator<String> iter = cmd.iterator();
        String[] next = Arrays.copyOf(index,index.length+1);
        while (iter.hasNext()) {
            String n = iter.next();
            next[next.length-1] = n;
            CommandActionWrapper ca = getCommand(next);
            if (ca != null && ca.getCommandAction() instanceof AbstractCompletable && !((AbstractCompletable) ca.getCommandAction()).processAutoComplete(arg))
                iter.remove();
        }
        return cmd;
    }

    private void registerCommand(String mainCommand, String[] split, CommandAction action, boolean fallBackOnSubcommandNotExist,boolean useCommandEvent) {
        // Recursive register completed!
        if (split.length == 0) {
            this.action = new CommandActionWrapper(action, currentIndex,useCommandEvent);
            this.fallBackOnSubcommandNotExist = fallBackOnSubcommandNotExist;
            return;
        }
        // Recursive register not completed, continuing...
        storage.computeIfAbsent(split[0], key -> new CommandStorage(mainCommand, currentIndex + 1))
                .registerCommand(
                        mainCommand, Arrays.copyOfRange(split, 1, split.length), action, fallBackOnSubcommandNotExist,useCommandEvent
                );
    }

    private CommandActionWrapper getCommandAction(String[] arg) {
        if (arg.length == 0)
            return this.action;
        if (storage.containsKey(arg[0]))
            return storage.get(arg[0]).getCommandAction(Arrays.copyOfRange(arg, 1, arg.length));
        if (fallBackOnSubcommandNotExist)
            return this.action;
        return null;
    }

    public static CommandActionWrapper getCommand(String[] argment) {
        return getMainStorage().getCommandAction(argment);
    }

    public static void getCompleteArgument(String[] cmd,List<String> complete,String last){
        CommandActionWrapper action = getCommand(cmd);
//        System.out.println("Command " + Arrays.toString(cmd) + "/" + action);
//        System.out.println(action.getCommandAction() instanceof AbstractCompletable);
        if(action != null && action.getCommandAction() instanceof AbstractCompletable)
            ((AbstractCompletable) action.getCommandAction()).editCompletion(cmd,complete,last);
    }



}
