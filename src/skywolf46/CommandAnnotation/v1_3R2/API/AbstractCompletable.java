package skywolf46.CommandAnnotation.v1_3R2.API;

import skywolf46.CommandAnnotation.v1_3R2.Data.CommandAction;
import skywolf46.CommandAnnotation.v1_3R2.Data.CommandArgument;

import java.util.List;

public abstract class AbstractCompletable extends CommandAction {
    public boolean processAutoComplete(CommandArgument arg) {
        return true;
    }

    public abstract void editCompletion(String[] commands,List<String> complete, String lastArgument);

    @Override
    public void active(Object[] o) {

    }
}
