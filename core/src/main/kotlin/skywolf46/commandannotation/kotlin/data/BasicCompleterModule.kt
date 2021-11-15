package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.AbstractAutoCompleteArgument
import skywolf46.commandannotation.kotlin.impl.FixedAutoCompleteArgument
import skywolf46.commandannotation.kotlin.impl.PlaceHoldingAutoCompleteArgument

class BasicCompleterModule(val completer: List<AbstractAutoCompleteArgument>) {
    companion object {
        fun analyze(str: String) = str.split(" ").run {
            val args = mutableListOf<AbstractAutoCompleteArgument>()
            for (x in this) {
                if (x.startsWith('<') && x.endsWith('>')) {
                    args += PlaceHoldingAutoCompleteArgument(x.substring(1, x.length - 1))
                } else {
                    args += FixedAutoCompleteArgument(*x.split("|").toTypedArray())
                }
            }
            return@run BasicCompleterModule(args)
        }
    }

    fun findCompleter(arg: Arguments): List<String> {
        val size = arg._sysPointer - 1
        if (size >= completer.size)
            return emptyList()
        return completer[size].filterArguments(arg._storage, arg.next())
    }
}