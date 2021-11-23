package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments

class DoubleCommandCondition : ICommandCondition {
    override fun parse(str: String?): ICommandCondition {
        return DoubleCommandCondition()
    }

    override fun isMatched(argument: Arguments, iterator: Arguments.ArgumentIterator): Boolean {
        if (!iterator.hasNext())
            return false
        iterator.peek().toDouble()
        iterator.store()
        return true
    }

    override fun getConditionPriority(): Int {
        return 1000000
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }


}