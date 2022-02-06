package skywolf46.commandannotation.v4.annotations

/**
 * Mark class as instantiable.
 *
 * If class is not marked as instantiable, CommandAnnotation will not register instance required command.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Instantiable(val autoInject: Boolean = true)
