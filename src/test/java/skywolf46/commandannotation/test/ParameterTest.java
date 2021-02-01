package skywolf46.commandannotation.test;

import org.junit.Test;
import skywolf46.commandannotation.annotations.CommandParam;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

import static org.junit.Assert.assertEquals;

public class ParameterTest {

    @Test
    public void testMethodParameterInjection() throws Throwable {
        ParameterStorage storage = ParameterStorage.of("Test1", "Test2", "Test4", 49, 60);
        storage.add("Test", "Hello World");
        ParameterMatchedInvoker invoker = new ParameterMatchedInvoker(ParameterTest.class.getMethod("test", String.class, String.class, int.class, String.class));
        invoker.invoke(storage);


    }

    public static void test(String x1, @CommandParam("Test") String x2, int x3, String x4) {
        assertEquals("Test1", x1);
        assertEquals("Hello World", x2);
        assertEquals(49, x3);
        assertEquals("Test2", x4);
    }


}
