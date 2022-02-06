package skywolf46.commandannotation.v4.conditions

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.data.Arguments

class NotCondition(val condition: AbstractCommandCondition) : AbstractCommandCondition() {
    override fun isPositive(args: Arguments): Boolean {
        return !condition.isPositive(args)
    }
}