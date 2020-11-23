package skywolf46.commandannotation.annotations;

public @interface $ {
    String value();

    String fallBack() default "";


    String checker() default "onError";
}
