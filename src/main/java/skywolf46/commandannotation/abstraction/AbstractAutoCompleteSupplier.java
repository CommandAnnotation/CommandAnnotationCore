package skywolf46.commandannotation.abstraction;

import skywolf46.commandannotation.util.ParameterStorage;

import java.util.List;

public abstract class AbstractAutoCompleteSupplier {
    public abstract void editAutoComplete(ParameterStorage storage,List<String> str);
}
