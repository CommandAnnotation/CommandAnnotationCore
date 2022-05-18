package skywolf46.commandannotation.v4.defines

import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher
import skywolf46.commandannotation.v4.api.annotations.define.CommandMatcher
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.PeekingIterator
import skywolf46.commandannotation.v4.initializer.CommandGeneratorCore

object CommandMatcherDefine {
    const val PRIORITY_GENERATE_REMAPPER = Integer.MAX_VALUE - 100000
    const val PRIORITY_EXECUTE_REMAPPER = Integer.MAX_VALUE - 1000
    const val PRIORITY_GENERATE_PURE_TEXT = Integer.MAX_VALUE - 1000
    const val PRIORITY_EXECUTE_PURE_TEXT = Integer.MAX_VALUE - 100000

    /**
     * Default argument handler.
     *
     * If parameter starts with <, and end with >,
     *  CommandAnnotation will detect as argument parameter,
     *  and will replace to runtime argument.
     */
    @CommandMatcher(PRIORITY_GENERATE_REMAPPER, PRIORITY_EXECUTE_REMAPPER)
    fun argumentParameterMatcher(iterator: PeekingIterator<String>): ICommandMatcher? {
        val str = iterator.next()
        if (str.startsWith('<') && str.endsWith('>')) {
            return ArgumentRemapperMatcher(str.substring(1, str.length - 1))
        }
        return null
    }


    /**
     * Fallback parameter handler.
     *
     * If none of [ICommandMatcher] mapped on parameter,
     *  CommandAnnotation will detect parameter as pure text parameter.
     *
     * Pure text parameter handler do not replace parameter, just return text value.
     */
    @CommandMatcher(PRIORITY_GENERATE_PURE_TEXT, PRIORITY_EXECUTE_PURE_TEXT)
    fun pureTextCommandHandler(iterator: PeekingIterator<String>): ICommandMatcher {
        return PureTextMatcher(iterator.next())
    }


    @Suppress("SpellCheckingInspection")
    private class ArgumentRemapperMatcher(val str: String) : ICommandMatcher {
        override fun isMatched(storage: Arguments, iter: PeekingIterator<String>): Boolean {
            iter.next()
            return true
        }

        override fun remap(storage: Arguments, iter: PeekingIterator<String>): Any? {
            return CommandGeneratorCore.remap(str, iter.next())
        }

        override fun hashCode(): Int {
            return str.hashCode()
        }

        override fun toString(): String {
            return "ArgumentRemapperMatcher(str='$str')"
        }

        override fun equals(other: Any?): Boolean {
            return other is ArgumentRemapperMatcher && other.str == str
        }

    }

    private class PureTextMatcher(val str: String) : ICommandMatcher {
        override fun isMatched(storage: Arguments, iter: PeekingIterator<String>): Boolean {
            return iter.next() == str
        }

        override fun remap(storage: Arguments, iter: PeekingIterator<String>): Any? {
            iter.next()
            return null
        }

        override fun equals(other: Any?): Boolean {
            return other is PureTextMatcher && other.str == str
        }

        override fun hashCode(): Int {
            return str.hashCode()
        }

        override fun toString(): String {
            return "PureTextMatcher(str='$str')"
        }
    }

}