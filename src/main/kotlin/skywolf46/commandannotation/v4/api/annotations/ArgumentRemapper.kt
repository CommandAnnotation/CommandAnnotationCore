package skywolf46.commandannotation.v4.api.annotations

@Repeatable
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ArgumentRemapper(vararg val value: String)
