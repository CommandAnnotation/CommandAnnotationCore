package skywolf46.commandannotation.exception.autocomplete;

public class AutoCompleteTypeMismatchException extends Exception {
    public AutoCompleteTypeMismatchException(Object o) {
        super("AutoComplete processed but " + o.getClass().getName() + " returned not String");
    }
}
