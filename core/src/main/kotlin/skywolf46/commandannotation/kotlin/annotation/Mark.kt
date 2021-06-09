package skywolf46.commandannotation.kotlin.annotation

/**
 * Mark current method to CommandAnnotation.
 * If [MarkVisibility] not set, visibility will set to [skywolf46.commandannotation.kotlin.enums.MarkVisibilityStatus.PROJECT].
 */
annotation class Mark(val name: String)
