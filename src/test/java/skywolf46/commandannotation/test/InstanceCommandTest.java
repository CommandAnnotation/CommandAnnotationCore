package skywolf46.commandannotation.test;

import org.junit.Test;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.util.ParameterStorage;

public class InstanceCommandTest {
    @Test(expected = IllegalAccessException.class)
    public void instanceCommandTest01() throws Throwable {
        ClassData cd = ClassData.create(new GlobalData(), getClass(), this).process(this);
        cd.getChain("test5").invoke(new ParameterStorage());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void instanceCommandTest02() throws Throwable {
        ClassData cd = ClassData.create(new GlobalData(), getClass(), this).process(this);
        cd.getChain("test6").invoke(new ParameterStorage());
    }

    @MinecraftCommand("/test5")
    public void helloWorld() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @MinecraftCommand("/test6")
    public void helloWorld2() throws IllegalAccessException {
        throw new IndexOutOfBoundsException();
    }
}
