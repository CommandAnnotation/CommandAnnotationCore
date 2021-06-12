package skywolf46.commandannotation.kotlin.annotation

import skywolf46.commandannotation.kotlin.enums.MarkVisibilityStatus

/**
 * Mark current method to CommandAnnotation.
 * If [MarkVisibility] not set, visibility will set to [skywolf46.commandannotation.kotlin.enums.MarkVisibilityStatus.PROJECT].
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Mark(
    val value: String,
    val scope: MarkVisibilityStatus = MarkVisibilityStatus.CLASS,
    val markPriority: Int = 0,
)
