package skywolf46.CommandAnnotation.v1_4R1.Data;

public class CommandActionWrapper {
    private CommandAction ca;
    private int index;
    private boolean useCommandEvent;

    public CommandActionWrapper(CommandAction ca, int index, boolean useCommandEvent) {
        this.ca = ca;
        this.index = index;
        this.useCommandEvent = useCommandEvent;
    }

    public CommandAction getCommandAction() {
        return ca;
    }

    public int getIndex() {
        return index;
    }

    public boolean useCommandEvent() {
        return useCommandEvent;
    }
}
