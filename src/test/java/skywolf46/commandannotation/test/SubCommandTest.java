package skywolf46.commandannotation.test;

import org.junit.Before;
import org.junit.Test;
import skywolf46.commandannotation.CommandAnnotation;
import skywolf46.commandannotation.annotations.common.SubCommand;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.test.data.ArgumentProvider;
import skywolf46.commandannotation.util.ParameterStorage;

import static org.junit.Assert.assertEquals;

public class SubCommandTest {
    private ClassData cd;
    private CommandArgument argument;

    @Before
    public void parameterSetup() {
        cd = ClassData.create(new GlobalData(), getClass()).process();
        argument = new ArgumentProvider("/test test1 test2 test3").getArgument();
    }

    @Test
    public void subCommandTest() throws Throwable {
        cd.getCommand("test01").invoke(ParameterStorage.of(argument));
    }

    @MinecraftCommand("/test01")
    public static void test(ParameterStorage storage) throws Throwable {
        CommandAnnotation.triggerSubCommand(SubCommandTest.class, "hello world 01", storage);
    }

    @SubCommand("hello world 01")
    public static void test(CommandArgument argument) {
        assertEquals("test1", argument.get(0));
    }
}
