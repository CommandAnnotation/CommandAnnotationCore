package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments

class IntCommandCondition : ICommandCondition {
    override fun parse(str: String?): ICommandCondition {
        return IntCommandCondition()
    }

    override fun isMatched(argument: Arguments, iterator: Arguments.ArgumentIterator): Boolean {
        iterator.peek().toInt()
        iterator.store()
        return true
    }


    override fun getConditionPriority(): Int {
        return 1000000
    }

    override fun equals(other: Any?): Boolean {
        return other is IntCommandCondition
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}