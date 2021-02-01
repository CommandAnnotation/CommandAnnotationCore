package skywolf46.commandannotation.test.data;

import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.util.ParameterStorage;

import java.util.Arrays;

public class ArgumentProvider {
    private CommandArgument argument;

    public ArgumentProvider(String commands) {
        String[] sp = commands.split(" ");
        argument = new CommandArgument(new ParameterStorage(), sp[0], Arrays.copyOfRange(sp, 1, sp.length), 0);
    }

    public CommandArgument getArgument() {
        return argument;
    }
}
