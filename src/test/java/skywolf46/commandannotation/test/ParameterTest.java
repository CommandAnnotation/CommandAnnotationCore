package skywolf46.commandannotation.test;

import skywolf46.commandannotation.annotations.CommandParam;
import skywolf46.commandannotation.annotations.common.ApplyGlobal;
import skywolf46.commandannotation.annotations.common.Mark;
import skywolf46.commandannotation.annotations.handler.error.ExceptHandler;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

public class ParameterTest {

    public static void main(String[] args) throws NoSuchMethodException {
        ParameterStorage storage = ParameterStorage.of("Test1", "Test2", "Test4", 49, 60);
        storage.add("Test", "Hello World");
        ParameterMatchedInvoker invoker = new ParameterMatchedInvoker(ParameterTest.class.getMethod("test", String.class, String.class, int.class, String.class));
        try {
            invoker.invoke(storage);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public static void test(String x1, @CommandParam("Test") String x2, int x3, String x4) {
        System.out.println(x1);
        System.out.println(x2);
        System.out.println(x3);
        System.out.println(x4);
    }


}
