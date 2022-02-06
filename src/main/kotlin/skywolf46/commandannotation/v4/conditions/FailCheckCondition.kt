package skywolf46.commandannotation.v4.conditions

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.data.Arguments
import skywolf46.extrautility.util.ifFalse

class FailCheckCondition(private val condition: AbstractCommandCondition, val failHandler: (Arguments) -> Unit) : AbstractCommandCondition() {
    override fun isPositive(args: Arguments): Boolean {
        return condition.isPositive(args).ifFalse {
            failHandler(args)
        }
    }
}