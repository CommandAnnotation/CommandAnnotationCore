package skywolf46.commandannotation.kotlin.annotation

import skywolf46.commandannotation.kotlin.enums.VisibilityScope

/**
 * Force apply [Mark] to this scope.
 * Scope will follow [Mark]'s scope.
 * **Warning** GLOBAL scope apply this mark to all project.
 * To prevent wrong usage, GLOBAL scope requires [SecurityBypass] annotation.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Force() {
}