package skywolf46.commandannotation.v4.annotations

import skywolf46.commandannotation.v4.enumerations.ImpactValue

/**
 * Bind an "Impact" value to function.
 * If function has bindable feature like [AutoComplete], CA will apply function to applicable function in matching impact condition.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Impact(val impact: ImpactValue, val value: String = "null")

fun Impact.getImpactValue(): String {
    return if (value == "null") impact.name else value
}