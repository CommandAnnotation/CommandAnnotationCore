package skywolf46.commandannotation.kotlin.annotation

/**
 * Provide autocomplete to command.
 * Because autocomplete will be based on system structure, some autocomplete features will not work. (Like discord)
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AutoComplete()