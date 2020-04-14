package skywolf46.CommandAnnotation.v1_4R1.API;

import skywolf46.CommandAnnotation.v1_4R1.Builder.MinecraftCommandBuilder;
import skywolf46.CommandAnnotation.v1_4R1.Data.CommandArgument;

import java.util.List;

public abstract class MinecraftAbstractCommand extends AbstractCompletable{
    public abstract boolean onCommand(CommandArgument args);
    public abstract int getCommandPriority();
    public boolean ignoreCommandCancel(){
        return false;
    }

    public static MinecraftCommandBuilder builder(){
        return new MinecraftCommandBuilder();
    }

    @Override
    public void editCompletion(String[] commands, List<String> complete, String lastArgument) {

    }
}