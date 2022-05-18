package skywolf46.commandannotation.v4.api.abstraction

import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.PeekingIterator

interface ICommandMatcher {

    fun isMatched(storage: Arguments, iter: PeekingIterator<String>): Boolean

    fun remap(storage: Arguments, iter: PeekingIterator<String>): Any?
}