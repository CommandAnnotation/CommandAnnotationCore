package skywolf46.commandannotation.v4.conditions

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.data.Arguments

class LambdaCondition(val unit: (Arguments) -> Boolean) : AbstractCommandCondition() {
    override fun isPositive(args: Arguments): Boolean {
        return unit(args)
    }

}