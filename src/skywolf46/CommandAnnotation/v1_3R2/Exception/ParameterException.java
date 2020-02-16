package skywolf46.CommandAnnotation.v1_3R2.Exception;

public class ParameterException extends RuntimeException {
    private Class c;

    public ParameterException(Class c, String msg) {
        super(msg);
        this.c = c;
    }

    public ParameterException(Class c) {
        super("Failed to parse parameter at class " + c + c.getSimpleName());
        this.c = c;
    }

    public Class getParameterClass() {
        return c;
    }
}
