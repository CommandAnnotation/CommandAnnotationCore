package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.commandannotation.kotlin.data.Arguments

interface ICommandCondition : Comparable<ICommandCondition> {
    fun parse(str: String?): ICommandCondition
    fun isMatched(iterator: Arguments.ArgumentIterator): Boolean

    fun getConditionPriority(): Int

    override fun equals(other: Any?): Boolean

    override fun compareTo(other: ICommandCondition): Int {
        return getConditionPriority().compareTo(other.getConditionPriority())
    }
}