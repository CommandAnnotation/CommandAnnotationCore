package skywolf46.commandannotation.v4.conditions

import skywolf46.commandannotation.v4.abstraction.ICommandCondition
import skywolf46.commandannotation.v4.data.Arguments

class OrCondition(val firstCondition: ICommandCondition, val secondCondition: ICommandCondition) : ICommandCondition {
    override fun isPositive(args: Arguments): Boolean {
        return firstCondition.isPositive(args) || secondCondition.isPositive(args)
    }
}