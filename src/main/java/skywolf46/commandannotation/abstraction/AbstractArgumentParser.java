package skywolf46.commandannotation.abstraction;

public abstract class AbstractArgumentParser {
    public abstract Class<?> getTargetClass();

    public abstract Object parse(Iterable<String> iterable) throws Exception;
}
