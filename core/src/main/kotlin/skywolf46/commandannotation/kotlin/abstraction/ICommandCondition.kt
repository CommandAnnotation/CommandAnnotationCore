package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.commandannotation.kotlin.data.Arguments

interface ICommandCondition : Comparable<ICommandCondition> {

    open fun findNextAutoComplete(argument: Arguments, includeSelf: Boolean): List<String> {
        return emptyList()
    }

    fun parse(str: String?): ICommandCondition

    fun isMatched(argument: Arguments, iterator: Arguments.ArgumentIterator): Boolean

    fun checkMatched(argument: Arguments): Boolean {
        val next = argument.clone()
        return isMatched(next, next.iterator())
    }


    fun checkLastCountMatched(argument: Arguments, depth: Int): Boolean {
        val next = argument.clone()
        return isLastCountMatched(next, next.size() - depth)
    }

    fun getConditionPriority(): Int

    // Check last counter is matched
    fun isLastCountMatched(args: Arguments, remaining: Int): Boolean {
        // Cause most of args requires 1 arg, default condition size will declare as 1
        return remaining == 1
    }

    override fun equals(other: Any?): Boolean

    override fun compareTo(other: ICommandCondition): Int {
        return getConditionPriority().compareTo(other.getConditionPriority())
    }
}