package skywolf46.commandannotation.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skywolf46.commandannotation.data.autocomplete.AutoCompleteSupplier;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.util.ParameterStorage;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MinecraftCommandImpl extends Command {
    private MethodChain chain;
    private HashMap<String, MinecraftCommandImpl> chainSubCommands = new HashMap<>();

    public MinecraftCommandImpl(String commandName) {
        super(commandName);
    }


    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        ParameterStorage storage = ParameterStorage.of(commandSender);
        long key = System.currentTimeMillis();
        CommandArgument arg = new CommandArgument(storage, s, strings, key);
        storage.set(arg);
        MinecraftCommandImpl implt = this;
        for (int i = 0; i < strings.length; i++) {
            if (implt.chainSubCommands.containsKey(strings[i])) {
                implt = implt.chainSubCommands.get(strings[i]);
                arg.nextPointer();
            }
        }
        implt.invokeCommand(storage);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        MinecraftCommandImpl impl = this;
        long key = System.currentTimeMillis();
        ParameterStorage stor = new ParameterStorage();
        CommandArgument arg = new CommandArgument(stor, alias, args, key);

        int i = 0;
        for (; i < args.length; i++) {
            if (impl.chainSubCommands.containsKey(args[i])) {
                impl = impl.chainSubCommands.get(args[i]);
                arg.nextPointer();
            } else break;
        }
        stor.set(arg);
        stor.add(sender);
        List<String> completer = impl.chainSubCommands.keySet().stream().filter(r -> args.length == 0 || args[args.length - 1].isEmpty() || r.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
        if (impl.chainSubCommands.size() <= 0 && args.length > 0) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.getName().toLowerCase().startsWith(args[args.length - 1])) {
                    completer.add(pl.getName());
                }
            }
        }
        if (impl.chain != null) {
            AutoCompleteSupplier sup = impl.chain.getCompleteSupplier();
            if (sup != null) {
                sup.editCompletion(stor, completer);
            }
        }
        return completer;
    }

    private void invokeCommand(ParameterStorage storage) {
        if (chain == null)
            return;
        chain.invoke(storage);
    }

    public void insert(String[] n, MethodChain chain) {
        if (n.length == 0) {
            this.chain = chain;
        } else {
            MinecraftCommandImpl impl = this;
            for (int i = 1; i < n.length; i++) {
                impl = impl.chainSubCommands.computeIfAbsent(n[i], a -> {
                    return new MinecraftCommandImpl("");
                });
            }
            impl.chain = chain;
        }
    }

    public void invokeSubCommand(CommandArgument arg) {

    }
}
