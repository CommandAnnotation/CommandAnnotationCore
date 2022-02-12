package skywolf46.commandannotation.v4.api.annotations.debug

/**
 * Indicates that the class is single-threaded only.
 *
 * Class can be used out of main thread, but code not contains concurrency support.
 * If method of [RequireSingleThread] annotated class instance called in multiple thread, it will occur visibility, or concurrency error.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class RequireSingleThread {
}