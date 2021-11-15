package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments

object FreeArgCondition : ICommandCondition {
    override fun parse(str: String?): ICommandCondition {
        return FreeArgCondition
    }

    override fun isMatched(argument: Arguments, iterator: Arguments.ArgumentIterator): Boolean {
        return true
    }

    // Always allowed
    override fun isLastCountMatched(args: Arguments, remaining: Int): Boolean {
        return true
    }

    override fun getConditionPriority(): Int {
        return 0
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