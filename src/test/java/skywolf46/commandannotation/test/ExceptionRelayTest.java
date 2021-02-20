package skywolf46.commandannotation.test;

import org.junit.Before;
import org.junit.Test;
import skywolf46.commandannotation.data.ExceptionRelay;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.data.parser.ArgumentParser;
import skywolf46.commandannotation.test.data.ArgumentProvider;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ExceptionRelayTest {
    private CommandArgument arg;

    @Before
    public void parameterSetup() {
        arg = new ArgumentProvider("/test test1 test2 test3").getArgument();
    }

    @Test
    public void relay() {
        ArgumentParser ap = arg.convert(Integer.class);
        AtomicInteger succeed = new AtomicInteger(0);
        if (!ap.handle(ExceptionRelay.relay()
                .handle(NumberFormatException.class, ex -> {
                    succeed.incrementAndGet();
                })
                .handle(Exception.class, ex -> {
                    succeed.incrementAndGet();
                })
                .handle(IllegalAccessError.class, ex -> {
                    System.out.println("What");
                })
        ))
            fail();
        assertEquals(2, succeed.get());
    }
}
