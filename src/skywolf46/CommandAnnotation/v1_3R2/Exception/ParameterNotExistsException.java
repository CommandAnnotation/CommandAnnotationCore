package skywolf46.CommandAnnotation.v1_3R2.Exception;

public class ParameterNotExistsException extends RuntimeException {
    private Class c;
    public ParameterNotExistsException(Class c){
        super("Failed to parse parameter at class " + c + c.getSimpleName() + " : Class parser not exists");
        this.c = c;
    }

    public Class getParameterClass() {
        return c;
    }
}
