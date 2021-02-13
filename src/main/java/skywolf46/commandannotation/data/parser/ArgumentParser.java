package skywolf46.commandannotation.data.parser;

import lombok.Getter;
import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.impl.define.*;
import skywolf46.commandannotation.data.parser.impl.minecraft.LocationDefine;
import skywolf46.commandannotation.data.parser.impl.minecraft.OfflinePlayerDefine;
import skywolf46.commandannotation.data.parser.impl.minecraft.PlayerDefine;
import skywolf46.commandannotation.data.parser.impl.minecraft.VectorDefine;
import skywolf46.commandannotation.util.ClassUtil;
import skywolf46.commandannotation.util.MinecraftChecker;
import skywolf46.commandannotation.util.PrimitiveConverter;

import java.util.HashMap;

public class ArgumentParser {
    private static HashMap<Class<?>, AbstractParseDefine<?>> defines = new HashMap<>();

    static {
        registerDefine(new IntegerDefine());
        registerDefine(new StringDefine());
        registerDefine(new StringArrayDefine(0, true));
        registerDefine(new IntegerArrayDefine(0));
        registerDefine(new DoubleDefine());
        if (MinecraftChecker.isMinecraft()) {
            registerDefine(new LocationDefine());
            registerDefine(new VectorDefine());
            registerDefine(new PlayerDefine());
            registerDefine(new OfflinePlayerDefine());
        }
    }

    private Object[] params;
    @Getter
    private int brokenPosition = -1;
    @Getter
    private Class<?> brokenClass = null;
    @Getter
    private Throwable brokenCause = null;

    public ArgumentParser(CommandArgument args, AbstractParseDefine<?>[] targets) {
        CommandArgument.CommandIterator iterator = args.iterator();
        params = new Object[targets.length];
        for (int i = 0; i < targets.length; i++) {
            AbstractParseDefine<?> define = targets[i];
            try {
//                System.out.println(iterator.left());
                params[i] = define.parse(iterator);
            } catch (Throwable ex) {
                brokenPosition = i;
                brokenCause = ex;
                brokenClass = targets[i].getType();
                return;
            }
        }
    }

    public <T> T get(int pointer) {
        return (T) params[pointer];
    }

    public <T> T get(Class<T> positional, int pointer) {
        return (T) params[pointer];
    }


    public int size() {
        return params.length;
    }

    public boolean isBroken() {
        return brokenPosition != -1;
    }

    public static AbstractParseDefine<?>[] toDefineCache(Object... objes) {
        AbstractParseDefine<Object>[] def;
        if (objes[0] == null) {
            // If null, array instance
            for (Object ox : objes)
                if (ox != null)
                    throw new IllegalStateException("Try to parse null definition");
            def = new AbstractParseDefine[1];
            def[0] = (AbstractParseDefine<Object>) defines.get(objes.getClass());
            if (def[0] == null)
                throw new ClassCastException("Cannot parse class " + ClassUtil.toObjectName(objes.getClass()) + ": Define not registered");
            def[0] = def[0].createInstance(objes);
        } else {
            def = new AbstractParseDefine[objes.length];
            for (int i = 0; i < def.length; i++) {
                if (objes[i] instanceof AbstractParseDefine<?>) {
                    def[i] = (AbstractParseDefine<Object>) objes[i];
                } else {
                    Class<?> target = objes[i] instanceof Class<?> ? (Class<?>) objes[i] : objes[i].getClass();
                    def[i] = (AbstractParseDefine<Object>) defines.get(target = PrimitiveConverter.boxPrimitive(target));
                    if (def[i] == null)
                        throw new ClassCastException("Cannot parse class " + ClassUtil.toObjectName(target) + ": Define not registered");
                    def[i] = def[i].createInstance(objes[i] instanceof Class ? null : objes[i]);
                }
            }
        }
        return def;
    }

    public static ArgumentParser parse(CommandArgument argument, Object... objes) {
        // NullCheck
        return new ArgumentParser(argument, toDefineCache(objes));
    }

    public static void registerDefine(AbstractParseDefine<?> definer) {
        defines.put(definer.getType(), definer);
    }


}
