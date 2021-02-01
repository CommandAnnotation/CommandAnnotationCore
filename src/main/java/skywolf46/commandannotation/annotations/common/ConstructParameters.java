package skywolf46.commandannotation.annotations.common;

public @interface ConstructParameters {
    Class<?>[] value();


    boolean exceptionWhenUnmatched() default true;
}
