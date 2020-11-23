package skywolf46.commandannotation.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface $_ {
    String value();

    boolean autoComplete() default true;

    boolean fallBackIfNotExists() default true;

    boolean useCommandEvent() default false;

//    Class<? extends AbstractAutoCompleterProvider> completer() default OnlinePlayerCompleter.class;

}
