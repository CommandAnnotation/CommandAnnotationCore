package skywolf46.commandannotation.v4.abstraction

import skywolf46.commandannotation.v4.data.Arguments

interface ICommandCondition {
    fun isPositive(args: Arguments) : Boolean
}