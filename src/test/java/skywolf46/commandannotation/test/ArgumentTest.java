package skywolf46.commandannotation.test;


import org.junit.Before;
import org.junit.Test;
import skywolf46.commandannotation.data.command.CommandArgument;
import skywolf46.commandannotation.test.data.ArgumentProvider;

import static org.junit.Assert.*;

public class ArgumentTest {
    private CommandArgument argument;

    @Before
    public void parameterSetup() {
        argument = new ArgumentProvider("/test test1 test2 test3").getArgument();
    }

    @Test
    public void testArgument01() {
        assertEquals("test1", argument.get(0));
    }


    @Test
    public void testArgument02() {
        argument.nextPointer();
        assertEquals("test2", argument.get(0));
    }


    @Test
    public void testArgument03() {
        argument.nextPointer();
        argument.nextPointer();
        assertEquals("test3", argument.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArgument04Overflow() {
        argument.nextPointer();
        argument.nextPointer();
        argument.nextPointer();
        assertNull(argument.get(0));
    }

    @Test
    public void testArgument01NullCheck() {
        assertNotNull(argument.getOrNull(0));
        assertNotNull(argument.getOrNull(1));
        assertNotNull(argument.getOrNull(2));
        assertNull(argument.getOrNull(3));
    }

    @Test
    public void testArgument02NullCheck() {
        argument.nextPointer();
        assertNotNull(argument.getOrNull(0));
        assertNotNull(argument.getOrNull(1));
        assertNull(argument.getOrNull(2));
    }

    @Test
    public void testArgument03NullCheck() {
        argument.nextPointer();
        argument.nextPointer();
        assertNotNull(argument.getOrNull(0));
        assertNull(argument.getOrNull(1));
    }

    @Test
    public void testArgument04NullCheck() {
        argument.nextPointer();
        argument.nextPointer();
        argument.nextPointer();
        assertNull(argument.getOrNull(0));
    }

    @Test
    public void testArgumentOriginalEquals() {
        for (int i = 0; i < argument.length(); i++) {
            assertEquals(argument.getOriginal(0), argument.get(0));
        }
    }

    @Test
    public void testArgumentOriginalNotEquals() {
        argument.nextPointer();
        for (int i = 0; i < argument.length(); i++) {
            assertNotEquals(argument.getOriginal(0), argument.get(0));
        }
    }

    @Test
    public void testArgumentOriginalEqualsBefore01() {
        argument.nextPointer();
        assertEquals("test1", argument.getArgumentBefore());
    }


    @Test
    public void testArgumentOriginalEqualsBefore02() {
        argument.nextPointer();
        argument.nextPointer();
        assertEquals("test1 test2", argument.getArgumentBefore());
    }

    @Test
    public void testArgumentOriginalEqualsBeforeException() {
        assertNull(argument.getArgumentBefore());
    }

}
