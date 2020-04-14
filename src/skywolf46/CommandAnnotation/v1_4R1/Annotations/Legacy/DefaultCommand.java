package skywolf46.CommandAnnotation.v1_4R1.Annotations.Legacy;

public @interface DefaultCommand {
    String command();
    Class<?>[] requireParameter() default {};
    Class<?>[] acceptableParameter() default {};
    boolean fallbackOnSubCommandNotExist() default true;

}
