package skywolf46.commandannotation.kotlin.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AutoCompleteProvider(val provider: String)