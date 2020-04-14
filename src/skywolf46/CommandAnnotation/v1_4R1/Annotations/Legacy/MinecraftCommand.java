package skywolf46.CommandAnnotation.v1_4R1.Annotations.Legacy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface MinecraftCommand {
    String[] command();
    Class<?>[] requireParameter() default {};
    Class<?>[] acceptableParameter() default {};
    boolean fallbackOnSubCommandNotExist() default true;
    boolean autoComplete() default false;
    boolean useCommandEvent() default false;
}
