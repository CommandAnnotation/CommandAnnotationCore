package skywolf46.CommandAnnotation.v1_3R2.API;

import skywolf46.CommandAnnotation.v1_3R2.Builder.MinecraftEventBuilder;
import skywolf46.CommandAnnotation.v1_3R2.Data.CommandArgument;

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
