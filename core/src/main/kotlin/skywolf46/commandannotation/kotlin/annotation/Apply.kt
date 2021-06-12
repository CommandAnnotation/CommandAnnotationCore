package skywolf46.commandannotation.kotlin.annotation

/**
 * Apply marked system to method.
 * Multiple mark apply is allowed.
 * If unique marked ability (like [AutoComplete]) is duplicated, only first mark will be applied.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Apply(vararg val value: String) {
}