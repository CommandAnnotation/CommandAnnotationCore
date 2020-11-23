package skywolf46.commandannotation.annotations.legacy;

public @interface DefaultCommand {
    String command();
    Class<?>[] requireParameter() default {};
    Class<?>[] acceptableParameter() default {};
    boolean fallbackOnSubCommandNotExist() default true;

}
