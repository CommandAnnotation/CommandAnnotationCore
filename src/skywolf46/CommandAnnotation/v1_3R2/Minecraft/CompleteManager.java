package skywolf46.CommandAnnotation.v1_3R2.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CompleteManager {
    private HashMap<String, CompleteManager> manager = new HashMap<>();

    public List<String> getComplete(String[] commands) {
        if (commands.length == 0)
            return new ArrayList<>();
        else if (commands.length == 1) {
            List<String> strs = new ArrayList<>();
            for (String n : manager.keySet())
                if (n.startsWith(commands[0]))
                    strs.add(n);
            return strs;
        }
        if (!manager.containsKey(commands[0]))
            return new ArrayList<>();
        return manager.get(commands[0])
                .getComplete(Arrays.copyOfRange(commands, 1, commands.length));
    }

    public void addComplete(String[] command) {
        if (command.length == 0)
            return;
        manager.computeIfAbsent(command[0], key -> new CompleteManager())
                .addComplete(Arrays.copyOfRange(command, 1, command.length));
    }
}
