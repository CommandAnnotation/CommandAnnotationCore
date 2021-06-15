package skywolf46.commandannotation.kotlin.annotation

import skywolf46.commandannotation.kotlin.enums.VisibilityScope

/**
 * Mark current method to CommandAnnotation.
 * If [MarkVisibility] not set, visibility will set to [skywolf46.commandannotation.kotlin.enums.VisibilityScope.PROJECT].
 *
 * @param force
 * If [force] is true, mark will not interrupted.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Mark(
    val value: String,
    val force: Boolean = true,
    val scope: VisibilityScope = VisibilityScope.CLASS,
    val markPriority: Int = 0,
)
