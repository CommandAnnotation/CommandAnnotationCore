package skywolf46.commandannotation.test;

import skywolf46.commandannotation.CommandAnnotation;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.test.starter.DenialStarter;
import skywolf46.commandannotation.test.starter.annotations.Denial;
import skywolf46.commandannotation.util.ParameterStorage;

public class StarterTest {
    public static void main(String[] args) {
        CommandAnnotation.registerScanTarget(new DenialStarter(null));
        ClassData cd = ClassData.create(new GlobalData(), StarterTest.class).process();
        cd.getChain("test").invoke(new ParameterStorage());
    }

    @MinecraftCommand("/test")
    @Denial(isDenial = true)
    public static void denial() {
        System.out.println("Hello, World!");
    }
}
