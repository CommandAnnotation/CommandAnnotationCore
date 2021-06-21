package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments

class DoubleCommandCondition : ICommandCondition{
    override fun parse(str: String?): ICommandCondition {
        TODO("Not yet implemented")
    }

    override fun isMatched(argument: Arguments, iterator: Arguments.ArgumentIterator): Boolean {
        TODO("Not yet implemented")
    }

    override fun getConditionPriority(): Int {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }
}