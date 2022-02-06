package skywolf46.commandannotation.v4.annotations

/**
 * Declare command provider.
 *
 * When [CommandProvider] attached to object method, or static method, CommandAnnotation will define new command provider with parameter, and return value.
 * Return value must have to implement [AbstractCommand] or define method with command define annotation.
 * You can find command define annotation in [skywolf46.commandannotation.v4.annotations.define].
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CommandProvider(val type: String)