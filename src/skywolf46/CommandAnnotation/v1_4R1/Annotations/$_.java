package skywolf46.CommandAnnotation.v1_4R1.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface $_ {
    String value();

    boolean autoComplete() default true;

    boolean fallBackIfNotExists() default true;

    boolean useCommandEvent() default false;

    String checker() default "";
}
