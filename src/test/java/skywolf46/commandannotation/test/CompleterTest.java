package skywolf46.commandannotation.test;

import org.junit.Test;
import skywolf46.commandannotation.annotations.autocomplete.AutoComplete;
import skywolf46.commandannotation.data.autocomplete.AutoCompleteSupplier;
import skywolf46.commandannotation.data.methodprocessor.ClassData;
import skywolf46.commandannotation.data.methodprocessor.GlobalData;
import skywolf46.commandannotation.data.methodprocessor.MethodChain;
import skywolf46.commandannotation.util.ParameterMatchedInvoker;
import skywolf46.commandannotation.util.ParameterStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CompleterTest {

    @Test
    public void completerTest() {
        try {
            AutoCompleteSupplier asp = AutoCompleteSupplier.from(new MethodChain(new ClassData(getClass(), new GlobalData()), new ParameterMatchedInvoker(getClass().getMethod("test"), null)));
            List<String> test = new ArrayList<>();
            asp.editCompletion(new ParameterStorage(), test);
            System.out.println(test);
            assertEquals(new ArrayList<>(Arrays.asList("Test1", "Test2")), test);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @AutoComplete
    public static List<String> test() {
        System.out.println("Test");
        return new ArrayList<>(Arrays.asList("Test1", "Test2"));
    }
}
