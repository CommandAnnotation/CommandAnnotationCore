package skywolf46.commandannotation.test;

import skywolf46.commandannotation.annotations.common.ApplyGlobal;
import skywolf46.commandannotation.annotations.common.Mark;
import skywolf46.commandannotation.annotations.handler.error.ExceptHandler;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.util.ParameterStorage;

public class CommandTest {

    public static void main(String[] args) {
        ClassData cd = ClassData.create(CommandTest.class);
        cd.getChain("test").invoke(new ParameterStorage());
    }

    @Mark("Test")
    @MinecraftCommand(command = "test")
    public static void test2() throws Exception {
        throw new NullPointerException();
    }

    @ExceptHandler
    @ApplyGlobal
    public static void handleException(Exception ex) {
        System.out.println("Exception!");
        System.out.println("Stacktrace printed");
    }



    @ExceptHandler(IllegalStateException.class)
    @ApplyGlobal
    public static void handleException2(Exception ex) {
        System.out.println("Illegal Exception!");
        System.out.println("Stacktrace printed");
    }


    @ExceptHandler(NullPointerException.class)
    @ApplyGlobal
    public static void handleException3(Exception ex) {
        System.out.println("NullPointer Exception!");
        System.out.println("Stacktrace printed");
    }
}
