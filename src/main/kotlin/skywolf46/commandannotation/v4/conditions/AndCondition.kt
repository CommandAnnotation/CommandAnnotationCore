package skywolf46.commandannotation.v4.conditions

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.data.Arguments

class AndCondition(
    val first: AbstractCommandCondition,
    val second: AbstractCommandCondition,
) : AbstractCommandCondition() {
    override fun isPositive(args: Arguments): Boolean {
        return first.isPositive(args) && second.isPositive(args)
    }
}