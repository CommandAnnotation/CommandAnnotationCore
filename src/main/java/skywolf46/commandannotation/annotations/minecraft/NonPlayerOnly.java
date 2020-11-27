package skywolf46.commandannotation.annotations.minecraft;

import skywolf46.commandannotation.annotations.common.StarterAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface NonPlayerOnly {
    String handler() default "";
}