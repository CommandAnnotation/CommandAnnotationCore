package skywolf46.CommandAnnotation.v1_3R2.Data;

import skywolf46.CommandAnnotation.v1_3R2.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_3R2.Exception.ParameterException;
import skywolf46.CommandAnnotation.v1_3R2.Exception.ParameterNotExistsException;

import java.util.HashMap;

public class ParameterIterator {
    private static HashMap<Class, ParameterParser> parser = new HashMap<>();
    private CommandData data;
    private int position;

    public ParameterIterator(CommandData data) {
        this.data = data;
        this.position = 0;
    }

    public <T> T next(Class<T> cls) {
        if (!parser.containsKey(cls)) {
            throw new ParameterNotExistsException(cls);
        }
        try {
            return (T) parser.get(cls).readParameter(this);
        } catch (Exception ex) {
//            ex.printStackTrace();
            throw new ParameterException(cls);
        }
    }

    public static void registerParser(Class c, ParameterParser pp) {
        parser.put(c, pp);
    }

    public String next() {
        return data.getCommandArgument(position++);
    }
}
