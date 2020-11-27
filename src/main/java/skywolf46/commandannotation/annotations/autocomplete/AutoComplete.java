package skywolf46.commandannotation.annotations.autocomplete;

//import skywolf46.commandannotation.autocompleter.OnlinePlayerCompleter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface AutoComplete {
    String value() default "";

//    Class<? extends AbstractAutoCompleterProvider> provider() default OnlinePlayerCompleter.class;
}
