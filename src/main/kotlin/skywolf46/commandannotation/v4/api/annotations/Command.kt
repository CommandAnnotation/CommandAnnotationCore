package skywolf46.commandannotation.v4.api.annotations

/**
 * Command definition of function.
 *
 * If command type is registered, CA will automatically scan classes, and register command.
 *
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Command(
    val section: String = "default",
    val type: String,
    val command: String,
    val alias: Array<String> = [],
)