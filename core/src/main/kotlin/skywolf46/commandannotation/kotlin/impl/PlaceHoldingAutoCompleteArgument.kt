package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.CommandAnnotationCore
import skywolf46.commandannotation.kotlin.abstraction.AbstractAutoCompleteArgument
import skywolf46.extrautility.data.ArgumentStorage

class PlaceHoldingAutoCompleteArgument(val holder: String) : AbstractAutoCompleteArgument() {
    override fun filter(storage: ArgumentStorage, str: String): MutableList<String> {
        return CommandAnnotationCore.getCompleter(holder)?.invoke(storage) ?: mutableListOf()
    }

}