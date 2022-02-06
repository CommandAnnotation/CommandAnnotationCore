package skywolf46.commandannotation.v4.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class AutoComplete(val name: String)
