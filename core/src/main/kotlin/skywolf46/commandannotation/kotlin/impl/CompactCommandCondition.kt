package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.MethodWrapper

class CompactCommandCondition : ICommandCondition {
    var conditionMethod: MethodInvoker? = null
    var autoCompleteMethod: MethodInvoker? = null
    override fun parse(str: String?): ICommandCondition {
        return this
    }

    override fun findNextAutoComplete(argument: Arguments, includeSelf: Boolean): List<String> {
        println("Compact completer : Called auto complete")
        return (autoCompleteMethod?.invoke(argument._storage) as Array<String>?)?.toList() ?: emptyList()
    }

    override fun isMatched(argument: Arguments, iterator: Arguments.ArgumentIterator): Boolean {
        return conditionMethod?.invoke(argument._storage) as Boolean? ?: true
    }

    override fun getConditionPriority(): Int {
        return 10000000
    }

    override fun equals(other: Any?): Boolean {
        return other === this
    }
}