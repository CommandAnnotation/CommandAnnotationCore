package skywolf46.CommandAnnotation.v1_4R1.Data;

import skywolf46.CommandAnnotation.v1_4R1.API.ParameterParser;
import skywolf46.CommandAnnotation.v1_4R1.Exception.ParameterException;
import skywolf46.CommandAnnotation.v1_4R1.Exception.ParameterNotExistsException;

import java.util.HashMap;

public class ParameterIterator {
    private static HashMap<Class, ParameterParser> parser = new HashMap<>();
    private static HashMap<String, ParameterParser> parserAsName = new HashMap<>();
    private CommandData data;
    private int position;
    private int snapshot;

    public ParameterIterator(CommandData data) {
        this.data = data;
        this.position = 0;
    }

    public void savePosition() {
        this.snapshot = position;
    }

    public void restorePosition() {
        this.position = snapshot;
    }

    public static boolean isParserExists(String data) {
        return parserAsName.containsKey(data);
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

    public static void registerParser(String[] name, Class c, ParameterParser pp) {
        parser.put(c, pp);
        for (String n : name)
            parserAsName.put(n, pp);
    }

    public String next() {
        return data.getCommandArgument(position++);
    }

    public <T> T next(String type) {
        if (!parserAsName.containsKey(type)) {
            throw new ParameterNotExistsException(type);
        }
        return (T) parserAsName.get(type).readParameter(this);
    }
}
