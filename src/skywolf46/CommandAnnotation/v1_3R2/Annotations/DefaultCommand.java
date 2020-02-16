package skywolf46.CommandAnnotation.v1_3R2.Annotations;

public @interface DefaultCommand {
    String command();
    Class<?>[] requireParameter() default {};
    Class<?>[] acceptableParameter() default {};
    boolean fallbackOnSubCommandNotExist() default true;

}
