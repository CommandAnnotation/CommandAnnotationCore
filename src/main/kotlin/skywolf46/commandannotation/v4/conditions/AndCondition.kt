package skywolf46.commandannotation.v4.conditions

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.data.Arguments

class AndCondition(val firstCondition: AbstractCommandCondition, val secondCondition: AbstractCommandCondition) : AbstractCommandCondition() {
    override fun isPositive(args: Arguments): Boolean {
        return firstCondition.isPositive(args) && secondCondition.isPositive(args)
    }
}