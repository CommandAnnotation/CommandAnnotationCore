package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments

class FixedStringCondition(val text: String) : ICommandCondition {
    override fun parse(str: String?): ICommandCondition {
        return FixedStringCondition("")
    }

    override fun isMatched(argument: Arguments, iterator: Arguments.ArgumentIterator): Boolean {
        if (!iterator.hasNext())
            return false
        return text == iterator.next()
    }

    override fun getConditionPriority(): Int {
        return -1
    }

    override fun equals(other: Any?): Boolean {
        return other is FixedStringCondition && other.text == text
    }

    override fun toString(): String {
        return "FixedStringCondition {$text}"
    }

    override fun isLastCountMatched(args: Arguments, remaining: Int): Boolean {
        println("Fixed condition : Last count: ${remaining}")
        return remaining == 1
    }

    override fun findNextAutoComplete(argument: Arguments, includeSelf: Boolean): List<String> {
        return mutableListOf(text)
    }

}