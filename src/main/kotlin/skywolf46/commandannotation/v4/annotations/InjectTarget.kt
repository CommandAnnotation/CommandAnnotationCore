package skywolf46.commandannotation.v4.annotations

import skywolf46.commandannotation.v4.constants.AnnotationConstant

/**
 * Mark class as inject target.
 *
 * If [name] is not defined, anonymous inject target will be generated.
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class InjectTarget(
    val name: String = AnnotationConstant.EMPTY_VALUE,
)