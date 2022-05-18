package skywolf46.commandannotation.v4.api.abstraction

import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.PeekingIterator

interface ICommandMatcher : Comparable<ICommandMatcher> {
    fun remap(storage: Arguments, iter: PeekingIterator<String>): Any?

    // TODO
    override fun compareTo(other: ICommandMatcher): Int {
        return 0
    }
}