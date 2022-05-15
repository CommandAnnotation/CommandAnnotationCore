package skywolf46.commandannotation.v4.test.data


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestCommandAnnotation(val command: String)