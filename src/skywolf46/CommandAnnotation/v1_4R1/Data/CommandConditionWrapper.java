package skywolf46.CommandAnnotation.v1_4R1.Data;

import skywolf46.CommandAnnotation.v1_4R1.Data.Auto.MCReflectiveAutoCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandConditionWrapper {
    private List<MCReflectiveAutoCommand> nextCommand = new ArrayList<>();

    public boolean canExecuted(CommandArgument data){
        for(MCReflectiveAutoCommand ref : nextCommand){
            if(ref.invoke(data))
                return true;
        }
        return false;
    }

    public void addAutoCommand(MCReflectiveAutoCommand ref){
        this.nextCommand.add(ref);
    }
}
