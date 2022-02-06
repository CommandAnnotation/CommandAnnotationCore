package skywolf46.commandannotation.v4.annotations

/**
 * Parent command definition on class.
 *
 * If CommandContainer is applied to subclass, it will concat with parent class function.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class CommandContainer(val type: String, val command: String, val alias: Array<String> = [])