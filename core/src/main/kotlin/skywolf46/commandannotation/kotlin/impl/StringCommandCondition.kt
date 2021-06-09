package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments

class StringCommandCondition : ICommandCondition {
    override fun parse(str: String?): ICommandCondition {
        return StringCommandCondition()
    }

    override fun isMatched(iterator: Arguments.ArgumentIterator): Boolean {
        iterator.next()
        return true
    }

    override fun getConditionPriority(): Int {
        return Integer.MAX_VALUE
    }

    override fun equals(other: Any?): Boolean {
        return other is StringCommandCondition
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}