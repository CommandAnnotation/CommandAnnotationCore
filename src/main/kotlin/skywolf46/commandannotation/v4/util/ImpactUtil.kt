package skywolf46.commandannotation.v4.util

import skywolf46.commandannotation.v4.annotations.Impact
import skywolf46.commandannotation.v4.enumerations.ImpactValue


private val scopeMap = mutableMapOf<String, () -> String>()
fun Impact.getImpactValue(): String {
    return if (value == "null") impact.name else value
}

fun ImpactValue.findScope() {

}