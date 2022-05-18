package skywolf46.commandannotation.v4.constants

import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.PeekingIterator

data class CommandMatcherWrapper(val matcher: ICommandMatcher, val priority: Int = 0) :
    Comparable<CommandMatcherWrapper> {

    fun isMatched(storage: Arguments, iter: PeekingIterator<String>): Boolean {
        return matcher.isMatched(storage, iter)
    }

    fun remap(storage: Arguments, iter: PeekingIterator<String>): Any? {
        return matcher.remap(storage, iter)
    }

    override fun compareTo(other: CommandMatcherWrapper): Int {
        return priority.compareTo(other.priority)
    }


    override fun equals(other: Any?): Boolean {
        return other is CommandMatcherWrapper && matcher == other.matcher
    }

    override fun hashCode(): Int {
        return matcher.hashCode()
    }

}