package skywolf46.commandannotation.test;


import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.util.ParameterStorage;

public class ArgumentTest {
    public static void main(String[] args) {
        long key = System.currentTimeMillis();
        CommandArgument arg = new CommandArgument(
                new ParameterStorage(),
                "test",
                new String[]{
                        "test1",
                        "test2",
                        "test3",
                },
                key
        );
        System.out.println(arg.get(0, arg.length()));
        arg.nextPointer();
        System.out.println(arg.get(0, arg.length()));
        arg.nextPointer();
        System.out.println(arg.get(0, arg.length()));
        arg.nextPointer();
//        System.out.println(arg.get(0));
//        arg.nextPointer(key);
    }
}
