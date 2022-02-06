package skywolf46.commandannotation.v4.annotations.debug

/**
 * Indicates that function is not for generic use, but for addon development.
 *
 * [AddonDevelopmentMethod] annotated method / function not have to use at normal case,
 *      and normally used for more addon features.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class AddonDevelopmentMethod