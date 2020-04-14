package skywolf46.CommandAnnotation.v1_4R1.Exception;

public class ParameterNotExistsException extends RuntimeException {
    private Class c;
    public ParameterNotExistsException(Class c){
        super("Failed to parse parameter at class " + c.getSimpleName() + " : Class parser not exists");
        this.c = c;
    }

    public ParameterNotExistsException(String type) {
        super("Failed to parse parameter at data name " + type+ " : Class parser not exists");

    }

    public Class getParameterClass() {
        return c;
    }
}
