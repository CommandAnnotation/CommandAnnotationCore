package skywolf46.commandannotation.v4.conditions

import skywolf46.commandannotation.v4.abstraction.ICommandCondition
import skywolf46.commandannotation.v4.data.Arguments

class NotCondition(val condition: ICommandCondition) : ICommandCondition {
    override fun isPositive(args: Arguments): Boolean {
        return !condition.isPositive(args)
    }
}