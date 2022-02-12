package skywolf46.commandannotation.v4.api.abstraction

import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.data.Requirement

abstract class AbstractCommandCondition {
    lateinit var requirement: Requirement
        internal set

    abstract fun isPositive(args: Arguments): Boolean

    open fun getPriority() = 0

    open fun compareAndSwap(condition: AbstractCommandCondition): AbstractCommandCondition {
        return if (getPriority() == condition.getPriority()) condition
        else if (getPriority() > condition.getPriority()) this
        else condition
    }

}