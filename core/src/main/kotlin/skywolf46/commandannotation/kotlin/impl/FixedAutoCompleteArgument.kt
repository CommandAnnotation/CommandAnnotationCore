package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.abstraction.AbstractAutoCompleteArgument
import skywolf46.extrautility.data.ArgumentStorage

class FixedAutoCompleteArgument(vararg val command: String) : AbstractAutoCompleteArgument() {
    override fun filter(storage: ArgumentStorage, str: String): MutableList<String> {
        return command.toMutableList()
    }

}