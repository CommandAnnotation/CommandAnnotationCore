package skywolf46.commandannotation.v4.defines

import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher
import skywolf46.commandannotation.v4.api.annotations.define.CommandMatcher

object CommandMatcherDefine {
    const val PRIORITY_LESS_THAN = Integer.MAX_VALUE / 2
    const val PRIORITY_PURE_TEXT = Integer.MAX_VALUE - 1000

    /**
     * Default argument handler.
     *
     * If parameter starts with <, and end with >,
     *  CommandAnnotation will detect as argument parameter,
     *  and will replace to runtime argument.
     */
    @CommandMatcher(PRIORITY_LESS_THAN)
    fun argumentCommandMatcher(str: String): ICommandMatcher? {
        if (str.startsWith('<') && str.endsWith('>')) {
            return TODO()
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
    @CommandMatcher(PRIORITY_PURE_TEXT)
    fun pureTextCommandHandler(): ICommandMatcher {
        return TODO()
    }


}