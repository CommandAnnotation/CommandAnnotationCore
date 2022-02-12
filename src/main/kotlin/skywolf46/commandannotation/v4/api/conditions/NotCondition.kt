package skywolf46.commandannotation.v4.api.conditions

import skywolf46.commandannotation.v4.api.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.api.data.Arguments

class NotCondition(val original: AbstractCommandCondition) : AbstractCommandCondition() {
    override fun isPositive(args: Arguments): Boolean {
        return !original.isPositive(args)
    }

}