package skywolf46.commandannotation.kotlin.annotation

/**
 * Apply marked system to method.
 * Multiple mark apply is allowed.
 * If unique marked ability (like [AutoComplete]) is duplicated, only first mark will be applied.
 */
annotation class Apply(vararg val value: String) {
}