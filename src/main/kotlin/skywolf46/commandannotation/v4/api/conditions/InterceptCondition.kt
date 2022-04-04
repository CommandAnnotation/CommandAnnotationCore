package skywolf46.commandannotation.v4.api.conditions

import skywolf46.commandannotation.v4.api.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.exceptions.CommandFailedException

class InterceptCondition(private val original: AbstractCommandCondition, private val handler: Arguments.() -> Unit) :
    AbstractCommandCondition() {
    override fun isPositive(args: Arguments): Boolean {
        if (!original.isPositive(args)) {
            handler(args)
            throw CommandFailedException("Fast-fail handler detected")
        }
        return true
    }

}