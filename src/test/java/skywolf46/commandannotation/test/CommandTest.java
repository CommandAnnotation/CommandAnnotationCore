package skywolf46.commandannotation.test;

import org.junit.Test;
import skywolf46.commandannotation.annotations.common.ApplyGlobal;
import skywolf46.commandannotation.annotations.handler.error.ExceptHandler;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.util.ParameterStorage;

import static org.junit.Assert.assertThrows;

public class CommandTest {

    @Test(expected = IllegalAccessException.class)
    public void testCommandInvokeException01() throws Throwable {
        ClassData cd = ClassData.create(new GlobalData(), CommandTest.class, null).process(null);
        cd.getChain("test").invoke(new ParameterStorage());
    }

    @Test(expected = SecurityException.class)
    public void testCommandInvokeException02() throws Throwable {
        ClassData cd = ClassData.create(new GlobalData(), CommandTest.class, null).process(null);
        cd.getChain("test2").invoke(new ParameterStorage());
    }


    @Test
    public void testCommandInvokeException03() {
        ClassData cd = ClassData.create(new GlobalData(), CommandTest.class, null).process(null);
        assertThrows(RuntimeException.class, () -> {
            cd.getChain("test3").invoke(new ParameterStorage());
        });
    }

    @MinecraftCommand("/test")
    public static void test() throws Exception {
        System.out.println("NPE!");
        throw new NullPointerException();
    }

    @MinecraftCommand("/test2")
    public static void test2() throws Exception {
        throw new IllegalStateException();
    }

    @MinecraftCommand("/test3")
    public static void test3() throws Exception {
        throw new RuntimeException();
    }


//    @ExceptHandler
//    @ApplyGlobal
//    public static void handleException(Exception ex) {
//        throw new RuntimeException();
//    }


    @ExceptHandler(IllegalStateException.class)
    @ApplyGlobal
    public static void handleException2(Exception ex) throws Exception {
        System.out.println("Illegal Handle");
        throw new SecurityException();
    }


    @ExceptHandler(NullPointerException.class)
    @ApplyGlobal
    public static void handleException3(Exception ex) throws Exception {
        System.out.println("Ill.");
        throw new IllegalAccessException();
    }
}
