package skywolf46.commandannotation.v4.api.abstraction

import skywolf46.commandannotation.v4.api.util.PeekingIterator

interface ICommandMatcher {
    fun isMatched(iter: PeekingIterator<String>)
}