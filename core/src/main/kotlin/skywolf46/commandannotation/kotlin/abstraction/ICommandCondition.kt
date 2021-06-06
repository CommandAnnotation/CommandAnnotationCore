package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.commandannotation.kotlin.data.Arguments

interface ICommandCondition {
    fun isMatched(iterator: Arguments.ArgumentIterator): Boolean

    fun getConditionPriority(): Int
}