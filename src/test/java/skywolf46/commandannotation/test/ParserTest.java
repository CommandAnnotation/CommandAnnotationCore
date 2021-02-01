package skywolf46.commandannotation.test;

import org.junit.Before;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.ArgumentParser;
import skywolf46.commandannotation.util.ParameterStorage;

import java.util.Arrays;

public class ParserTest {
    public static void main(String[] args) {
        CommandArgument arguments = new CommandArgument(new ParameterStorage(), "a",
                "1 2 3 4 Five".split(" "), 0);
        ArgumentParser ap = ArgumentParser.parse(arguments, ParameterTest.class);
        if (ap.isBroken()) {
            System.out.println("Broken!");
            ap.getBrokenCause().printStackTrace();
            return;
        }
        for (int i = 0; i < ap.size(); i++) {
            System.out.println((Object) ap.get(i));
        }
        System.out.println(Arrays.toString(ap.get(String[].class, 0)));
    }

    @Before
    private void argumentDeclare() {

    }
}


