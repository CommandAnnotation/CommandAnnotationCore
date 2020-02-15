package skywolf46.CommandAnnotation.v1_3.Data;

import java.util.Arrays;

public class CommandData {
    private String totalCommand;
    private String command;
    private int currentIndex;
    private String[] commandArgs;
    private String[] currentArgs;

    public CommandData(String cmd, int index) {
        this.totalCommand = cmd;
        this.currentIndex = index;
        this.commandArgs = cmd.split(" ");
        this.command = commandArgs[0];
        this.commandArgs = Arrays.copyOfRange(commandArgs, 1, commandArgs.length);
        currentArgs = Arrays.copyOfRange(commandArgs, index, commandArgs.length);
    }

    public String[] getCommandArgument() {
        return Arrays.copyOf(currentArgs, currentArgs.length);
    }

    public String[] getRealCommandArgument() {
        return Arrays.copyOf(commandArgs, commandArgs.length);
    }

    public String getCommand() {
        return command;
    }

    public String getCommandArgument(int from, int to) {
        return getConnectedString(from, to, currentArgs);
    }

    public String getRealCommandArgument(int from, int to) {
        return getConnectedString(from, to, commandArgs);
    }

    public String getCommandArgument(int index) {
        return currentArgs[index];
    }

    public String getRealCommandArgument(int index) {
        return commandArgs[index];
    }

    private String getConnectedString(int from, int to, String[] commandArgs) {
        if (from < 0)
            throw new IllegalArgumentException("Command argument starting index is illegal : Starting point cannot lower than 0");
        else if (from > commandArgs.length)
            throw new IllegalArgumentException("Command argument starting index is illegal : Starting point cannot higher than " + commandArgs.length);
        else if (from > to)
            throw new IllegalArgumentException("Command argument starting index is illegal : Starting point cannot higher than ending index");
        if (to > commandArgs.length)
            throw new IllegalArgumentException("Command argument ending index is illegal : Ending point cannot higher than " + commandArgs.length);
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < to; i++)
            if (sb.length() > 0) {
                sb.append(' ').append(commandArgs[i]);
            } else
                sb.append(commandArgs[i]);
        return sb.toString();
    }

    public ParameterIterator iterator() {
        return new ParameterIterator(this);
    }

    public int length() {
        return currentArgs.length;
    }
}
