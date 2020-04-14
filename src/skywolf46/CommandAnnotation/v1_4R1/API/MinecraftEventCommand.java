package skywolf46.CommandAnnotation.v1_4R1.API;

import skywolf46.CommandAnnotation.v1_4R1.Builder.MinecraftEventBuilder;
import skywolf46.CommandAnnotation.v1_4R1.Data.CommandArgument;

public abstract class MinecraftEventCommand {
    public abstract boolean onEvent(CommandArgument ca);

    public abstract int getEventPriority();

    public abstract boolean supplyEventArgument();

    public boolean ignoreCommandCancel() {
        return false;
    }


    public static MinecraftEventBuilder builder() {
        return new MinecraftEventBuilder();
    }
}
